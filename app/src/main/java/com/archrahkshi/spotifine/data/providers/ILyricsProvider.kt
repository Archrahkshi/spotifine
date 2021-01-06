package com.archrahkshi.spotifine.data.providers

import org.jsoup.Connection

interface ILyricsProvider: BaseProvider {
    fun getNoLyricsMessage(): List<String>
    fun getUnidentifiableLanguageMessage(): String
    fun getDefaultTranslateButtonText(): String
    fun getTranslateButtonText(isLangIdentified: Boolean, language: String): String
    fun getTranslatingSuccess(): Boolean
}