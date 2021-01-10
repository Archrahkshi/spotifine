package com.archrahkshi.spotifine.data.providerImpls

import android.content.Context
import com.archrahkshi.spotifine.data.providers.IUserPreferences
import com.archrahkshi.spotifine.data.providers.IUserPreferencesAccessor

class UserPreferencesImpl(context: Context): IUserPreferences, IUserPreferencesAccessor {
    companion object {
        const val KEY_PREFERENCES = "Key_pref"
        const val KEY_LANG = "lang"
        const val KEY_FULLSCREEN = "fullscreen"
        const val DEFAULT_LOCALE_SELECTION = true
        const val DEFAULT_FULLSCREEN_SELECTION = true
    }

    private val preferences = context.getSharedPreferences(KEY_PREFERENCES, Context.MODE_PRIVATE)

    override fun getIsEnglishLocale() = preferences.getBoolean(KEY_LANG, DEFAULT_LOCALE_SELECTION)
    override fun getFullscreenModeSelection() = preferences.getBoolean(KEY_FULLSCREEN, DEFAULT_FULLSCREEN_SELECTION)

    override fun setLanguage(isEnglishLanguageSelected: Boolean) {
        preferences.edit().putBoolean(KEY_LANG, isEnglishLanguageSelected).apply()
    }
    override fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        preferences.edit().putBoolean(KEY_FULLSCREEN, isFullscreenModeSelected).apply()
    }
}