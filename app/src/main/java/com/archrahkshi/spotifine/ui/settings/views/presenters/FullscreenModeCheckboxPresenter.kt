package com.archrahkshi.spotifine.ui.settings.views.presenters

import com.archrahkshi.spotifine.data.factories.UserPreferencesFactory
import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.ui.settings.views.IFullscreenModeCheckbox

class FullscreenModeCheckboxPresenter(private val viewState: IFullscreenModeCheckbox) : Provider {
    override val provider = UserPreferencesFactory.instance!!

    fun setSelectedFullscreenMode() {
        viewState.setFullscreenModeCheckboxSelection(provider.getFullscreenModeSelection())
    }

    fun setFullscreenMode(isFullscreenModeSelected: Boolean) {
        provider.setFullscreenMode(isFullscreenModeSelected)
        setSelectedFullscreenMode()
    }
}
