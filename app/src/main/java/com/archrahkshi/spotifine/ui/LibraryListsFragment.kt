package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.ACCESS_TOKEN
import com.archrahkshi.spotifine.data.ALBUMS
import com.archrahkshi.spotifine.data.ARTISTS
import com.archrahkshi.spotifine.data.ARTIST_FROM_ME_DISTINCTION
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.FROM_ARTIST
import com.archrahkshi.spotifine.data.FROM_ME
import com.archrahkshi.spotifine.data.IMAGE
import com.archrahkshi.spotifine.data.LIST_TYPE
import com.archrahkshi.spotifine.data.LibraryListsAdapter
import com.archrahkshi.spotifine.data.NAME
import com.archrahkshi.spotifine.data.PLAYLISTS
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.URL
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_library_lists.recyclerViewLists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class LibraryListsFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val accessToken = arguments?.getString(ACCESS_TOKEN)

        launch {
            recyclerViewLists.adapter = LibraryListsAdapter(
                createLibraryLists(accessToken)!!
            ) {
                Log.i("Item clicked", it.toString())
                fragmentManager?.beginTransaction()?.replace(
                    R.id.frameLayoutLibrary,
                    when (it) {
                        is Playlist -> TracksFragment().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                                putString(IMAGE, it.image)
                                putString(NAME, it.name)
                                putString(ACCESS_TOKEN, accessToken)
                            }
                        }
                        is Artist -> LibraryListsFragment().apply {
                            arguments = Bundle().apply {
                                putString(LIST_TYPE, ALBUMS)
                                putString(URL, it.url)
                                putString(IMAGE, it.image)
                                putString(ACCESS_TOKEN, accessToken)
                            }
                        }
                        is Album -> TracksFragment().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                                putString(IMAGE, it.image)
                                putString(NAME, it.name)
                                putString(ACCESS_TOKEN, accessToken)
                            }
                        }
                        else -> null
                    }!!
                )?.addToBackStack(null)?.commit()
            }
        }
    }

    private suspend fun createLibraryLists(
        accessToken: String?
    ) = withContext(Dispatchers.IO) {
        when (arguments?.getString(LIST_TYPE)) {
            PLAYLISTS -> {
                val libraryLists = mutableListOf<Playlist>()
                getJsonFromApi(
                    "me/playlists",
                    accessToken
                )["items"].asJsonArray.forEach {
                    libraryLists.add(createPlaylist(it.asJsonObject))
                }
                libraryLists
            }
            ARTISTS -> {
                val libraryLists = mutableListOf<Artist>()
                getJsonFromApi(
                    "me/following?type=artist",
                    accessToken
                )["artists"].asJsonObject["items"].asJsonArray.forEach {
                    libraryLists.add(createArtist(it.asJsonObject))
                }
                libraryLists
            }
            ALBUMS -> {
                val libraryLists = mutableListOf<Album>()
                val json = getJsonFromApi(
                    "${arguments?.getString(URL) ?: "me"}/albums",
                    accessToken
                )
                val items = json["items"].asJsonArray
                when (
                    json["href"]
                        .asString
                        .removePrefix("https://api.spotify.com/v1/")
                        .take(ARTIST_FROM_ME_DISTINCTION)
                ) {
                    "ar" -> {
                        items.forEach {
                            libraryLists.add(createAlbum(it.asJsonObject, FROM_ARTIST))
                        }
                        libraryLists
                    }
                    "me" -> {
                        items.forEach {
                            libraryLists.add(
                                createAlbum(it.asJsonObject["album"].asJsonObject, FROM_ME)
                            )
                        }
                        libraryLists
                    }
                    else -> libraryLists
                }
            }
            else -> null
        }
    }

    private fun createPlaylist(item: JsonObject): Playlist {
        val tracks = item["tracks"].asJsonObject
        return Playlist(
            image = item["images"].asJsonArray.first().asJsonObject["url"].asString,
            name = item["name"].asString,
            size = tracks["total"].asInt,
            url = tracks["href"].asString
        )
    }

    private fun createArtist(item: JsonObject) = Artist(
        image = item["images"].asJsonArray[1].asJsonObject["url"].asString,
        name = item["name"].asString,
        url = "artists/${item["id"].asString}"
    )

    private fun createAlbum(item: JsonObject, type: String) = Album(
        image = item["images"].asJsonArray[1].asJsonObject["url"].asString,
        name = item["name"].asString,
        artists = item["artists"].asJsonArray
            .joinToString { it.asJsonObject["name"].asString },
        url = if (type == FROM_ARTIST)
            "${item["href"].asString}/tracks"
        else
            item["tracks"].asJsonObject["href"].asString
    )

    private fun getJsonFromApi(requestPostfix: String, accessToken: String?) = JsonParser().parse(
        try {
            OkHttpClient().newCall(
                Request.Builder()
                    .url("https://api.spotify.com/v1/$requestPostfix")
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
}
