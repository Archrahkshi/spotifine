package com.archrahkshi.spotifine.data.providers

import java.util.Locale

interface IUserPreferences: BaseProvider {
    fun getIsEnglishLocale(): Boolean
    fun getFullscreenModeSelection(): Boolean
}