package com.archrahkshi.spotifine.ui.commonViews

import android.view.View
import android.widget.ImageView
import android.widget.TextView

interface IToolbar {
    fun setTitle(title: String)
    fun applyBackButton(isHidden: Boolean)
}

class ToolbarImpl(
    private val tvTitle: TextView,
    private val buttonBack: ImageView
) : IToolbar {
    override fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun applyBackButton(isHidden: Boolean) {
        buttonBack.visibility = if (isHidden) View.GONE else View.VISIBLE
    }
}
