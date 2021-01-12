package com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters

import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val viewState: IToolbar) {
    fun setupToolbar(title: String) {
        viewState.apply {
            setTitle(title)
            showBackButton(true)
            hideSettingsButton()
        }
    }
}
