package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters

import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val viewState: IToolbar) {
    companion object {
        const val TOOLBAR_TITLE = "Library"
    }

    fun setupToolbar(name: String?, isAlbums: Boolean) {
        with(viewState) {
            if (isAlbums && name != null) {
                setTitle(name)
                showBackButton(true)
            } else {
                setTitle(TOOLBAR_TITLE)
                showBackButton(false)
            }
        }
    }
}
