package com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters

import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val viewState: IToolbar) {
    companion object {
        const val BACK_BUTTON_VISIBILITY = true
    }

    fun setupToolbar(title: String) {
        viewState.setTitle(title)
        viewState.showBackButton(BACK_BUTTON_VISIBILITY)
    }
}