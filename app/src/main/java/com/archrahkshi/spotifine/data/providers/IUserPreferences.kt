package com.archrahkshi.spotifine.data.providers

interface IUserPreferences : BaseProvider {
    fun getIsEnglishLocale(): Boolean
    fun getFullscreenModeSelection(): Boolean
}
