package com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters

import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.ui.commonViews.IToolbar

class ToolbarPresenter(private val fragment: Fragment, private val viewState: IToolbar) {
    fun setupToolbar() {
        with(viewState) {
            setTitle(fragment.getString(R.string.tracks))
            showBackButton(true)
        }
    }
}
