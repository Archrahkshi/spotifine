package com.archrahkshi.spotifine.ui.commonViews

import android.view.View
import android.widget.ImageView
import android.widget.TextView

interface IToolBar {
    fun setTitle(title: String)
    fun applyBackButton(isHidden: Boolean)
}

class ToolBarImpl(
    private val tvTitle: TextView,
    private val buttonBack: ImageView
) : IToolBar {
    override fun setTitle(title: String) {
        tvTitle.text = title
    }

    override fun applyBackButton(isHidden: Boolean) {
        buttonBack.visibility = if (isHidden) View.INVISIBLE else View.VISIBLE
    }
}