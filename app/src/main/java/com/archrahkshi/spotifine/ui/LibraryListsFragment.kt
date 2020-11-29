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

class LibraryListsFragment<ListType>(override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate) :
    Fragment(), CoroutineScope {
    private fun getJsonStringFromHttpGet(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        return try {
            val responses = client.newCall(request).execute()
            responses.body?.string().toString()
        } catch (e: IOException) {
            null
        }
    }
/*    private inline fun <reified T> checkType() =  when (T::class){
        Playlist::class -> "playlist"
        Artist::class -> "artist"
        Album::class -> "album"
        else -> "another"
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        val flag = arguments?.getString("flag") ?: "no flag"
        Log.wtf("flag", flag)
    
        val parser = JsonParser()
        suspend fun libraryListCreation() = withContext(Dispatchers.IO) {
            when (flag) {
                "playlist" -> {
        
                    val playlistJson =
                        getJsonStringFromHttpGet("https://api.spotify.com/v1/me/playlists")
                    Log.wtf("json", playlistJson)
                    val jsonTree = parser.parse(playlistJson)
                    val libraryLists = mutableListOf<Playlist>()
                    if (jsonTree.isJsonObject) {
                        val jsonObject = jsonTree.asJsonObject
                        Log.wtf(
                            "trackUri",
                            jsonObject["items"].asJsonArray[0].asJsonObject["tracks"].asJsonObject["href"].toString()
                        )
                        val playlistsNumber = jsonObject["total"].asInt
                        Log.wtf("total", playlistsNumber.toString())
                        if (playlistsNumber > 0) {
                            for (i in 0 until playlistsNumber) {
                                val path = jsonObject["items"].asJsonArray[i]
                                val name =
                                    path.asJsonObject["name"].toString().removeSurrounding("\"")
                                val size = path.asJsonObject["tracks"].asJsonObject["total"].asInt
                                val url =
                                    path.asJsonObject["tracks"].asJsonObject["href"].toString()
                                        .removeSurrounding("\"")
                                libraryLists.add(Playlist(0, name, size, url))
                            }
                        }
                    }
                    libraryLists
                }
                "artist" -> {
                    val libraryLists = mutableListOf<Artist>()
                    libraryLists
                }
                "album" -> {
                    val libraryLists = mutableListOf<Album>()
                    libraryLists
                }
                else -> {
                    val libraryLists = mutableListOf<Artist>()
                    libraryLists
                }
            }
        }
    
        launch {
            val libraryLists = libraryListCreation()
            Log.wtf("librarylist", libraryLists.first().toString())
            recyclerViewLists.adapter = LibraryListsAdapter(libraryLists) {
                Log.wtf("it", it.toString())
                fragmentManager?.beginTransaction()?.replace(
                    R.id.frameLayoutLibrary,
                    when (it) {
                        is Playlist -> TracksFragment().apply {
            
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                            }
                        }
                        is Artist -> LibraryListsFragment<Album>().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                            }
                        }
                        is Album -> TracksFragment().apply {
                            arguments = Bundle().apply {
                                putString(URL, it.url)
                            }
                        }
                        else -> null // TODO: разобраться с sealed классами и убрать этот костыль
                    }!!
                )?.addToBackStack(null)?.commit()
            }


            //val listOfRequests = listOf("https://api.spotify.com/v1/me/playlists", "https://api.spotify.com/v1/me/albums", "https://api.spotify.com/v1/me/artists")


        }

        //val libraryLists = listOf<ListType>() // TODO


    }
}