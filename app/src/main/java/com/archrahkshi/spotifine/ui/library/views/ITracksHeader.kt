package com.archrahkshi.spotifine.ui.library.views

import android.widget.ImageView
import android.widget.TextView
import com.archrahkshi.spotifine.util.EMPTY_STRING
import com.bumptech.glide.Glide

interface ITracksHeader {
    fun setText(text: String)
    fun setSubtext(text: String?)
    fun setAdditionalText(text: String)
    fun setImage(uri: String)
}

class TracksHeaderImpl(
    private val textView: TextView,
    private val subTextView: TextView,
    private val additionalTextView: TextView,
    private val imageView: ImageView
) : ITracksHeader {
    override fun setText(text: String) {
        textView.text = text
    }

    override fun setSubtext(text: String?) {
        subTextView.text = text ?: EMPTY_STRING
    }

    override fun setAdditionalText(text: String) {
        additionalTextView.text = text
    }

    override fun setImage(uri: String) {
        Glide.with(textView.context).load(uri).into(imageView)
    }
}
