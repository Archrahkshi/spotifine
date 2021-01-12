package com.archrahkshi.spotifine.data.providerImpls

import android.net.Uri
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.providers.ILibraryListProvider
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.LibraryListsFragment
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.ARTIST_FROM_USER_DISTINCTION
import com.archrahkshi.spotifine.util.FROM_ARTIST
import com.archrahkshi.spotifine.util.FROM_USER
import com.archrahkshi.spotifine.util.PLAYLISTS
import com.archrahkshi.spotifine.util.SPOTIFY_PREFIX
import com.archrahkshi.spotifine.util.asAlbum
import com.archrahkshi.spotifine.util.asArtist
import com.archrahkshi.spotifine.util.asPlaylist
import com.archrahkshi.spotifine.util.getJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibraryListProviderImpl(private val fragment: LibraryListsFragment) : ILibraryListProvider {
    override suspend fun getList(url: String?, listType: String, accessToken: String?) =
        withContext(Dispatchers.IO) {
            when (listType) {
                PLAYLISTS -> with("${SPOTIFY_PREFIX}me/tracks".getJson(accessToken)) {
                    "${SPOTIFY_PREFIX}me/playlists"
                        .getJson(accessToken)["items"]
                        .asJsonArray
                        .map { it.asJsonObject.asPlaylist() }
                        .toMutableList()
                        .apply {
                            add(
                                0,
                                Playlist(
                                    image = Uri.parse(
                                        "android.resource://com.archrahkshi.spotifine/" +
                                            R.drawable.favorites
                                    ).toString(),
                                    name = fragment.getString(R.string.favorites),
                                    size = this@with["total"].asInt,
                                    url = this@with["href"].asString
                                )
                            )
                        }
                }
                ARTISTS ->
                    "${SPOTIFY_PREFIX}me/following?type=artist"
                        .getJson(accessToken)["artists"]
                        .asJsonObject["items"]
                        .asJsonArray
                        .map { it.asJsonObject.asArtist() }
                ALBUMS -> with("$SPOTIFY_PREFIX${url ?: "me"}/albums".getJson(accessToken)) {
                    val items = this["items"].asJsonArray
                    when (
                        this["href"]
                            .asString
                            .removePrefix(SPOTIFY_PREFIX)
                            .take(ARTIST_FROM_USER_DISTINCTION)
                    ) {
                        "ar" -> items.map { it.asJsonObject.asAlbum(FROM_ARTIST) }
                        "me" -> items.map {
                            it.asJsonObject["album"].asJsonObject.asAlbum(FROM_USER)
                        }
                        else -> listOf()
                    }
                }
                else -> listOf()
            }
        }
}
