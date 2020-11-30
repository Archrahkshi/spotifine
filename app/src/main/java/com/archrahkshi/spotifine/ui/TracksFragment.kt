package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.*
import com.bumptech.glide.Glide
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_tracks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

class TracksFragment(override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate) : Fragment(), CoroutineScope {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)
    
    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val token = this.arguments?.getString(ACCESS_TOKEN)
        val url = this.arguments?.getString(URL).toString()
        val image = this.arguments?.getString(IMAGE).toString()
        textViewHeaderLine1.text = this.arguments?.getString(NAME).toString()
        
        Glide
            .with(this)
            .load(image)
            .into(imageView2)
        
        launch {
            recyclerViewTracks.adapter = TracksAdapter(
                createTrackLists(url, token)
            ) {
                Log.i("Track", it.toString())
                startActivity(Intent(activity, PlayerActivity::class.java))
            }
        }
    }
    private fun getJsonFromApi(url: String, accessToken: String?) = JsonParser().parse(
        try {
            OkHttpClient().newCall(
                Request.Builder()
                    .url(url)
                    .header("Authorization", "Bearer $accessToken")
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build()
            ).execute().body?.string().also {
                Log.i("JSON string", it ?: "no JSON string")
            }
        } catch (e: IOException) {
            Log.wtf("getJsonFromApi", e)
            null
        }
    ).asJsonObject
    
    private suspend fun createTrackLists(
        url: String,
        accessToken: String?
    ) = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<Track>()
        val json = getJsonFromApi(url, token)
        when (json["href"].asString.removePrefix("https://api.spotify.com/v1/").take(5)) {
            "album" -> {
                json["items"].asJsonArray.forEach { element ->
                    val item = element.asJsonObject
                    tracks.add(
                        Track(
                            name = item["name"].asString,
                            artist = item["artists"].asJsonArray
                                .joinToString { it.asJsonObject["name"].asString },
                            duration = item["duration_ms"].asLong,
                            url = item["href"].asString
                        )
                    )
                }
                tracks
            }
            "playl" -> {
                json["items"].asJsonArray.forEach { element ->
                    val item = element.asJsonObject["track"].asJsonObject
                    tracks.add(
                        Track(
                            name = item["name"].asString,
                            artist = item["artists"].asJsonArray
                                .joinToString { it.asJsonObject["name"].asString },
                            duration = item["duration_ms"].asLong,
                            url = item["href"].asString
                        )
                    )
                }
                tracks
            }
            else -> tracks
        }
    }
}