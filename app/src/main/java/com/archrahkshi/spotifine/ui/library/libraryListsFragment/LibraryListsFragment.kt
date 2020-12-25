package com.archrahkshi.spotifine.ui.library.libraryListsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.LibraryListsAdapter
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.ui.library.tracksFragment.TracksFragment
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.ARTIST_FROM_USER_DISTINCTION
import com.archrahkshi.spotifine.util.FROM_ARTIST
import com.archrahkshi.spotifine.util.FROM_USER
import com.archrahkshi.spotifine.util.IMAGE
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.PLAYLISTS
import com.archrahkshi.spotifine.util.SIZE
import com.archrahkshi.spotifine.util.SPOTIFY_PREFIX
import com.archrahkshi.spotifine.util.URL
import com.archrahkshi.spotifine.util.createAlbum
import com.archrahkshi.spotifine.util.createArtist
import com.archrahkshi.spotifine.util.createPlaylist
import com.archrahkshi.spotifine.util.getJsonFromApi
import kotlinx.android.synthetic.main.fragment_library_lists.recyclerViewLists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LibraryListsFragment(
        override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {

    private val presenter by lazy { LibraryListsPresenter(this) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.applyToolbar()

        val accessToken = arguments?.getString(ACCESS_TOKEN)

        launch {
            recyclerViewLists.adapter = LibraryListsAdapter(createLibraryLists(accessToken)) {
                fragmentManager?.beginTransaction()?.replace(
                        R.id.frameLayoutLibrary,
                        when (it) {
                            is Playlist -> TracksFragment().apply {
                                arguments = Bundle().apply {
                                    putString(ACCESS_TOKEN, accessToken)
                                    putString(IMAGE, it.image)
                                    putString(NAME, it.name)
                                    putInt(SIZE, it.size)
                                    putString(URL, it.url)
                                }
                            }
                            is Artist -> LibraryListsFragment().apply {
                                arguments = Bundle().apply {
                                    putString(ACCESS_TOKEN, accessToken)
                                    putString(IMAGE, it.image)
                                    putString(NAME, it.name)
                                    putString(LIST_TYPE, ALBUMS)
                                    putString(URL, it.url)
                                }
                            }
                            is Album -> TracksFragment().apply {
                                arguments = Bundle().apply {
                                    putString(ACCESS_TOKEN, accessToken)
                                    putString(ARTISTS, it.artists)
                                    putString(IMAGE, it.image)
                                    putString(NAME, it.name)
                                    putInt(SIZE, it.size)
                                    putString(URL, it.url)
                                }
                            }
                        }
                )?.setTransition(TRANSIT_FRAGMENT_FADE)?.addToBackStack(null)?.commit()
            }
        }
    }

    private suspend fun createLibraryLists(accessToken: String?) = withContext(Dispatchers.IO) {
        when (arguments?.getString(LIST_TYPE)) {
            PLAYLISTS -> getJsonFromApi(
                    "${SPOTIFY_PREFIX}me/playlists",
                    accessToken
            )["items"].asJsonArray.map { createPlaylist(it.asJsonObject) }
            ARTISTS -> getJsonFromApi(
                    "${SPOTIFY_PREFIX}me/following?type=artist",
                    accessToken
            )["artists"].asJsonObject["items"].asJsonArray.map { createArtist(it.asJsonObject) }
            ALBUMS -> {
                val json = getJsonFromApi(
                        "$SPOTIFY_PREFIX${arguments?.getString(URL) ?: "me"}/albums",
                        accessToken
                )
                val items = json["items"].asJsonArray
                when (
                    json["href"]
                            .asString
                            .removePrefix(SPOTIFY_PREFIX)
                            .take(ARTIST_FROM_USER_DISTINCTION)
                    ) {
                    "ar" -> items.map { createAlbum(it.asJsonObject, FROM_ARTIST) }
                    "me" -> items.map {
                        createAlbum(it.asJsonObject["album"].asJsonObject, FROM_USER)
                    }
                    else -> listOf()
                }
            }
            else -> listOf()
        }
    }
}
