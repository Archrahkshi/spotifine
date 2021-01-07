package com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters

import com.archrahkshi.spotifine.data.factories.LyricsProviderFactory
import com.archrahkshi.spotifine.data.providers.Provider
import com.archrahkshi.spotifine.util.identifyLanguage
import com.archrahkshi.spotifine.util.translateFromTo
import com.ibm.watson.language_translator.v3.util.Language.RUSSIAN
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class LyricsPresenter(private val viewState: ILyrics) : Provider {
    override val provider = LyricsProviderFactory.instance!!

    fun load(originalLyrics: String?) {
        CoroutineScope(Main).launch {
            viewState.loading()
            if (originalLyrics == null)
                viewState.setupLyrics(provider.getNoLyricsMessage(), null)
            else {
                val language = originalLyrics.identifyLanguage()
                if (language == RUSSIAN)
                    viewState.setupLyrics(originalLyrics.split('\n'), null)
                else {
                    if (!provider.getTranslatingSuccess())
                        viewState.setupLyrics(
                            originalLyrics.split('\n'),
                            provider.getDefaultTranslateButtonText()
                        )
                    else
                        with(originalLyrics.translateFromTo(language, RUSSIAN)) {
                            if (this != null)
                                viewState.setupLyrics(
                                    this.split('\n'),
                                    provider.getTranslateButtonText(true, language)
                                )
                            else
                                viewState.setupLyrics(
                                    provider.getUnidentifiableLanguageMessage().split('\n'),
                                    null
                                )
                        }
                    viewState.applyButtonTranslate(originalLyrics)
                }
            }
            viewState.loaded()
        }
    }
}
