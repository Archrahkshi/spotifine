package com.archrahkshi.spotifine.ui.commonViews.presenters

import com.archrahkshi.spotifine.data.factories.UserPreferencesFactory
import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.ui.commonViews.IFullscreenMode

class FullscreenModePresenter(private val viewState: IFullscreenMode) : Provider {
    override val provider = UserPreferencesFactory.instance!!
    fun setSelectionFullscreenMode() {
        viewState.setFullscreenMode(provider.getFullscreenModeSelection())
    }
}
