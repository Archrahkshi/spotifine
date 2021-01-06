package com.archrahkshi.spotifine.data.providersImpls

import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.providers.ILyricsProvider
import com.archrahkshi.spotifine.ui.player.lyricsFragment.LyricsFragment
import com.archrahkshi.spotifine.util.IS_LYRICS_TRANSLATED
import com.ibm.watson.language_translator.v3.util.Language.RUSSIAN
import com.ibm.watson.language_translator.v3.util.Language.ENGLISH
import java.util.Locale

class LyricsProviderImpl(private val fragment: LyricsFragment): ILyricsProvider {
    override fun getNoLyricsMessage() = listOf(fragment.getString(R.string.no_lyrics))
    override fun getUnidentifiableLanguageMessage() = fragment.getString(R.string.unidentifiable_language)
    override fun getDefaultTranslateButtonText() = fragment.getString(R.string.translate)
    override fun getTranslatingSuccess() = fragment.arguments?.getBoolean(IS_LYRICS_TRANSLATED) ?: false
    override fun getTranslateButtonText(isLangIdentified: Boolean, language: String) =
        fragment.getString(
            R.string.detected_language,
            if (!isLangIdentified)
                fragment.getString(R.string.elvish) // Language kinda identified, yet badly
            else
                Locale(language).getDisplayLanguage(Locale(ENGLISH)),
            Locale(RUSSIAN).getDisplayLanguage(Locale(ENGLISH))
        )
}