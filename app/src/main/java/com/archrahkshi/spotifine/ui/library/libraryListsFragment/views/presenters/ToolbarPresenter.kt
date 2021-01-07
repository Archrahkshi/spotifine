package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters

import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val viewState: IToolbar) {
    companion object {
        const val TOOLBAR_TITLE = "Library"
    }

    fun setupToolbar(name: String?, isAlbums: Boolean) {
        if (isAlbums && name != null) {
            viewState.setTitle(name)
            viewState.showBackButton(true)
        } else {
            viewState.setTitle(TOOLBAR_TITLE)
            viewState.showBackButton(false)
        }
    }
}