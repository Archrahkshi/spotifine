package com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters

import com.archrahkshi.spotifine.ui.library.tracksFragment.views.ITracksHeader


class TracksHeaderPresenter(private val viewState: ITracksHeader) {
    fun setText(text: String) = viewState.setText(text)
    fun setSubtext(text: String?) = viewState.setSubtext(text)
    fun setAdditionalText(text: String) = viewState.setAdditionalText(text)
    fun setImage(img: String) = viewState.setImage(img)
}
