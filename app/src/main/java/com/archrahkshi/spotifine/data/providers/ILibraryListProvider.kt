package com.archrahkshi.spotifine.data.providers

import com.archrahkshi.spotifine.data.ListType

interface ILibraryListProvider : BaseProvider {
    suspend fun getList(url: String?, listType: String, accessToken: String?): List<ListType>
}
