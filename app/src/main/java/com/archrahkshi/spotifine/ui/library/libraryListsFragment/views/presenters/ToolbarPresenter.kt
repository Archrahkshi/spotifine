package com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters

import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val fragment: Fragment, private val viewState: IToolbar) {
    fun setupToolbar(name: String?, isAlbums: Boolean) {
        with(viewState) {
            if (isAlbums && name != null) {
                setTitle(name)
                showBackButton(true)
            } else {
                setTitle(fragment.getString(R.string.title_library))
                showBackButton(false)
            }
        }
    }
}
