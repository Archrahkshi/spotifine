package com.archrahkshi.spotifine.data.providers

interface ITrackDataProvider : BaseProvider {
    fun getAccessToken(): String
    fun getArtists(): String
    fun getName(): String
}
