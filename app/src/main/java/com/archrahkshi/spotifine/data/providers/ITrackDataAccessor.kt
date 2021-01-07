package com.archrahkshi.spotifine.data.providers

interface ITrackDataAccessor {
    fun setAccessToken(accessToken: String)
    fun setArtists(artists: String)
    fun setName(name: String)
}
