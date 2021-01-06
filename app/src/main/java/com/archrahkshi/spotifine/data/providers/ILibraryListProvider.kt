package com.archrahkshi.spotifine.data.providers

import com.archrahkshi.spotifine.data.ListType
import com.archrahkshi.spotifine.data.Track

interface ILibraryListProvider: BaseProvider {
    suspend fun getList(url: String?, listType: String, accessToken: String?): List<ListType>
}