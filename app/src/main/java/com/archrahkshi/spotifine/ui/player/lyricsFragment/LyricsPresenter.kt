package com.archrahkshi.spotifine.ui.player.lyricsFragment

import com.archrahkshi.spotifine.ui.commonViews.ToolBarImpl
import com.archrahkshi.spotifine.util.NAME
import kotlinx.android.synthetic.main.toolbar.imgBack
import kotlinx.android.synthetic.main.toolbar.tvTitle

class LyricsPresenter(private val fragment: LyricsFragment) {
    private val activity = fragment.requireActivity()

    private val toolBarImpl
        by lazy { ToolBarImpl(activity.tvTitle, activity.imgBack) }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun hideBackButton() = toolBarImpl.applyBackButton(true)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    fun applyToolbar() {
        setToolbarTitle(fragment.requireArguments().getString(NAME)!!)
        showBackButton()
    }

}