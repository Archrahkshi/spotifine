package com.archrahkshi.spotifine.ui.player.lyricsFragment

import com.archrahkshi.spotifine.ui.commonViews.ToolbarImpl
import com.archrahkshi.spotifine.util.NAME
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText

class LyricsPresenter(private val fragment: LyricsFragment) {
    private val activity = fragment.requireActivity()

    private val toolBarImpl by lazy {
        ToolbarImpl(
            activity.textViewToolbarText,
            activity.imageViewBack
        )
    }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    fun applyToolbar() {
        setToolbarTitle(fragment.requireArguments().getString(NAME)!!)
        showBackButton()
    }
}
