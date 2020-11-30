package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.*
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_library_lists.*
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        launch {
            recyclerViewLists.adapter = LibraryListsAdapter(
                createLibraryLists(arguments?.getString(ACCESS_TOKEN))!!
            ) {
                Log.i("Item clicked", it.toString())
                fragmentManager?.beginTransaction()?.replace(
                    R.id.frameLayoutLibrary,
                    when (it) {
                        is Playlist -> TracksFragment().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                                //putString(ACCESS_TOKEN, arguments?.getString(ACCESS_TOKEN))
                            }
                        }
                        is Artist -> LibraryListsFragment().apply {
                            arguments = Bundle().apply {
                                putString(LIST_TYPE, ALBUMS)
                                putString(URL, it.url)
                                //putString(ACCESS_TOKEN, arguments?.getString(ACCESS_TOKEN))
                            }
                        }
                        is Album -> TracksFragment().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                                //putString(ACCESS_TOKEN, arguments?.getString(ACCESS_TOKEN))
                            }
                        }
                        else -> null // TODO: разобраться с sealed классами и убрать этот костыль
                    }!!
                )?.addToBackStack(null)?.commit()
            }
        }
    }
    
    private suspend fun createLibraryLists(accessToken: String?) = withContext(Dispatchers.IO) {
        when (arguments?.getString(LIST_TYPE)) {
            PLAYLISTS -> {
                val libraryLists = mutableListOf<Playlist>()
                getJsonFromApi("me/playlists", accessToken)["items"].asJsonArray.forEach {
                    val item = it.asJsonObject
                    val tracks = item["tracks"].asJsonObject
                    libraryLists.add(
                        Playlist(
                            name = item["name"].asString,
                            size = tracks["total"].asInt,
                            url = tracks["href"].asString
                        )
                    )
                }
                libraryLists
            }
            ARTISTS -> {
                val libraryLists = mutableListOf<Artist>()
                getJsonFromApi(
                    // список альбомов исполнителя ?: пользователя
                     "me/following?type=artist",
                    accessToken
                )["artists"].asJsonObject["items"].asJsonArray.forEach {
                    val item = it.asJsonObject
                    libraryLists.add(
                        Artist(
                            name = item["name"].asString,
                            url = "artists/${item["id"].asString}"
                        )
                    )
                }
                libraryLists
            }
            ALBUMS -> {
                val libraryLists = mutableListOf<Album>()
                val json =  getJsonFromApi("${arguments?.getString(URL) ?: "me"}/albums", token)
                when(json["href"].asString.removePrefix("https://api.spotify.com/v1/").take(2)){
                    "ar" -> {
                        json["items"].asJsonArray.forEach {
                            val artists = it.asJsonObject["artists"].asJsonArray
                            var artistsNames = ""
                            for (i in artists){
                                artistsNames += "${i.asJsonObject["name"].asString}, "
                            }
                            libraryLists.add(
                                Album(
                                    name = it.asJsonObject["name"].asString,
                                    artists = artistsNames.removeSuffix(", "),
                                    url = "${it.asJsonObject["href"].asString}/tracks"
                                )
                            )
                        }
                        Log.wtf("Album", libraryLists.joinToString())
                        libraryLists
                    }
                    "me" -> {
                        json["items"].asJsonArray.forEach {
                            val album = it.asJsonObject["album"].asJsonObject
                            val artists = album["artists"].asJsonArray
                            var artistsNames = ""
                            for (i in artists){
                                artistsNames += "${i.asJsonObject["name"].asString}, "
                            }
                            libraryLists.add(
                                Album(
                                    name = album["name"].asString,
                                    artists = artistsNames.removeSuffix(", "),
                                    url = album["tracks"].asJsonObject["href"].asString
                                )
                            )

                        }
                        Log.wtf("Album", libraryLists.joinToString())
                        libraryLists
                    }
                    else -> {
                        libraryLists
                    }
                }
            }
            else -> null
        }
    }
    
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