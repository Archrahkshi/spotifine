package com.archrahkshi.spotifine.data.providerImpls

import com.archrahkshi.spotifine.data.providers.ITrackDataAccessor
import com.archrahkshi.spotifine.data.providers.ITrackDataProvider

class TrackDataProviderImpl: ITrackDataProvider, ITrackDataAccessor {
    private object TrackDataRepository {
        var accessToken: String? = null
        var artists: String? = null
        var name: String? = null
    }

    override fun getAccessToken() = TrackDataRepository.accessToken!!
    override fun getArtists() = TrackDataRepository.artists!!
    override fun getName() = TrackDataRepository.name!!

    override fun setAccessToken(accessToken: String) {
        TrackDataRepository.accessToken = accessToken
    }

    override fun setArtists(artists: String) {
        TrackDataRepository.artists = artists
    }

    override fun setName(name: String) {
        TrackDataRepository.name = name
    }
}