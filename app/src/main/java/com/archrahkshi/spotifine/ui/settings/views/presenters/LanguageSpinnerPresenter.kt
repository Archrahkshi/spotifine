package com.archrahkshi.spotifine.ui.settings.views.presenters

import android.util.Log
import com.archrahkshi.spotifine.data.factories.UserPreferencesFactory
import com.archrahkshi.spotifine.data.providers.BaseProvider
import com.archrahkshi.spotifine.data.providers.IUserPreferences
import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.ui.settings.views.ILanguageSpinner

class LanguageSpinnerPresenter(private val viewState: ILanguageSpinner): Provider {
    override val provider = UserPreferencesFactory.instance!!

    fun setLanguage(isEnglishLanguageSelected: Boolean) {
        provider.setLanguage(isEnglishLanguageSelected)
        setSelectedLanguage()
    }

    fun setSelectedLanguage() = viewState.setLanguage(provider.getIsEnglishLocale())
}