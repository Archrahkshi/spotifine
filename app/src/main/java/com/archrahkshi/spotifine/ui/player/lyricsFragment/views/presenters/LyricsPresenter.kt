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
            viewState.apply {
                loading()
                if (originalLyrics == null) {
                    setupLyrics(provider.getNoLyricsMessage(), null)
                    loaded(successfully = false)
                }
                else {
                    val targetLanguage = RUSSIAN
                    val identifiedLanguage = originalLyrics.identifyLanguage()
                    if (identifiedLanguage == targetLanguage) {
                        setupLyrics(
                            originalLyrics.split('\n'),
                            provider.getDefaultTranslateButtonText()
                        )
                        loaded(successfully = false)
                    }
                    else {
                        if (!provider.getTranslatingSuccess())
                            setupLyrics(
                                originalLyrics.split('\n'),
                                provider.getDefaultTranslateButtonText()
                            )
                        else
                            with(
                                originalLyrics.translateFromTo(identifiedLanguage, targetLanguage)
                            ) {
                                if (this != null)
                                    setupLyrics(
                                        split('\n'),
                                        provider.getTranslateButtonText(
                                            true,
                                            identifiedLanguage,
                                            targetLanguage
                                        )
                                    )
                                else
                                    setupLyrics(
                                        provider.getUnidentifiableLanguageMessage().split('\n'),
                                        null
                                    )
                            }
                        applyButtonTranslate(originalLyrics)
                        loaded(successfully = true)
                    }
                }
            }
        }
    }
}
