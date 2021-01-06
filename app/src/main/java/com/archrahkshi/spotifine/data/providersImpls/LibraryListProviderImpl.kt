package com.archrahkshi.spotifine.data.providersImpls

import com.archrahkshi.spotifine.data.ListType
import com.archrahkshi.spotifine.data.providers.ILibraryListProvider
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.ARTIST_FROM_USER_DISTINCTION
import com.archrahkshi.spotifine.util.FROM_ARTIST
import com.archrahkshi.spotifine.util.FROM_USER
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.PLAYLISTS
import com.archrahkshi.spotifine.util.SPOTIFY_PREFIX
import com.archrahkshi.spotifine.util.URL
import com.archrahkshi.spotifine.util.asAlbum
import com.archrahkshi.spotifine.util.asArtist
import com.archrahkshi.spotifine.util.asPlaylist
import com.archrahkshi.spotifine.util.getJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LibraryListProviderImpl: ILibraryListProvider  {
    // TODO: исправить на GSON
    override suspend fun getList(url: String?, listType: String, token: String?) =
        withContext(Dispatchers.IO) {
            when (listType) {
                PLAYLISTS ->
                    "${SPOTIFY_PREFIX}me/playlists"
                        .getJson(token)["items"]
                        .asJsonArray
                        .map { it.asJsonObject.asPlaylist() }
                ARTISTS ->
                    "${SPOTIFY_PREFIX}me/following?type=artist"
                        .getJson(token)["artists"]
                        .asJsonObject["items"]
                        .asJsonArray
                        .map { it.asJsonObject.asArtist() }
                ALBUMS -> {
                    val json = "$SPOTIFY_PREFIX${url ?: "me"}/albums"
                        .getJson(token)
                    val items = json["items"].asJsonArray
                    when (
                        json["href"]
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