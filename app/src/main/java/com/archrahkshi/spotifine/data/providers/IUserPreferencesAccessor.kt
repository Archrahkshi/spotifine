package com.archrahkshi.spotifine.data.providers

interface IUserPreferencesAccessor {
    fun setLanguage(isEnglishLanguageSelected: Boolean)
    fun setFullscreenMode(isFullscreenModeSelected: Boolean)
}
