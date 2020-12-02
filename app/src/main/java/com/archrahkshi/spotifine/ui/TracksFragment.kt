package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Track
import com.archrahkshi.spotifine.data.TracksAdapter
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUM_FROM_PLAYLIST_DISTINCTION
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.DURATION
import com.archrahkshi.spotifine.util.ID
import com.archrahkshi.spotifine.util.IMAGE
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SIZE
import com.archrahkshi.spotifine.util.URL
import com.archrahkshi.spotifine.util.setWordTracks
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_tracks.imageViewHeader
import kotlinx.android.synthetic.main.fragment_tracks.recyclerViewTracks
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine1
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine2
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

class TracksFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments

        textViewHeaderLine1.text = args?.getString(NAME)

        val artists = args?.getString(ARTISTS)
        if (artists != null)
            textViewHeaderLine2.text = artists
        else
            textViewHeaderLine2.visibility = View.GONE

        val size = args?.getInt(SIZE)
        textViewHeaderLine3.text = getString(
            R.string.header_line3,
            size,
            setWordTracks(context, size)
        )

        Glide.with(this).load(args?.getString(IMAGE)).into(imageViewHeader)

        launch {
            recyclerViewTracks.adapter = TracksAdapter(
                createTrackLists(args?.getString(URL), args?.getString(ACCESS_TOKEN))
            ) {
                Log.i("Track clicked", it.toString())
                startActivity(
                    Intent(activity, PlayerActivity::class.java).apply {
                        putExtra(ID, it.id)
                        putExtra(DURATION, it.duration)
                        putExtra(NAME, it.name)
                        putExtra(ARTISTS, it.artists)
                    }
                )
            }
        }
    }

    private fun getJsonFromApi(url: String?, accessToken: String?) = JsonParser().parse(
        try {
            OkHttpClient().newCall(
                Request.Builder()
                    .url(url!!)
                    .header("Authorization", "Bearer $accessToken")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build()
            ).execute().body?.string()
        } catch (e: IOException) {
            Log.wtf("getJsonFromApi", e)
            null
        }
    ).asJsonObject

    private suspend fun createTrackLists(
        url: String?,
        accessToken: String?
    ) = withContext(Dispatchers.IO) {
        val json = getJsonFromApi(url, accessToken)
        val items = json["items"].asJsonArray
        when (
            json["href"]
                .asString
                .removePrefix("https://api.spotify.com/v1/")
                .take(ALBUM_FROM_PLAYLIST_DISTINCTION)
        ) {
            "album" -> items.map { createTrack(it.asJsonObject) }
            "playl" -> items.map { createTrack(it.asJsonObject["track"].asJsonObject) }
            else -> listOf()
        }
    }

    private suspend fun createTrack(item: JsonObject) = withContext(Dispatchers.IO) {
        Track(
            name = item["name"].asString,
            artists = item["artists"].asJsonArray
                .joinToString { it.asJsonObject["name"].asString },
            duration = item["duration_ms"].asLong,
            id = item["id"].asString
        )
    }
}
