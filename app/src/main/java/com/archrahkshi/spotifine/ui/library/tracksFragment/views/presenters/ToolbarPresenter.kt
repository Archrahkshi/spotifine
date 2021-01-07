package com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters

import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val viewState: IToolbar) {
    companion object {
        const val TOOLBAR_TITLE = "Tracks"
        const val BACK_BUTTON_VISIBILITY = true
    }

    fun setupToolbar() {
        viewState.setTitle(TOOLBAR_TITLE)
        viewState.showBackButton(BACK_BUTTON_VISIBILITY)
    }
}
