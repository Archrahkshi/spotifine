package com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters

interface ILyrics {
    fun setupLyrics(lyrics: List<String>, buttonText: String?)
    fun applyButtonTranslate(originalLyrics: String)
    fun loading()
    fun loaded()
}