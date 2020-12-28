package com.archrahkshi.spotifine.ui.player.lyricsFragment

import android.app.Instrumentation
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.LyricsAdapter
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.IS_LYRICS_TRANSLATED
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.ORIGINAL_LYRICS
import com.archrahkshi.spotifine.util.getOriginalLyrics
import com.archrahkshi.spotifine.util.identifyLanguage
import com.archrahkshi.spotifine.util.translateFromTo
import com.ibm.watson.language_translator.v3.util.Language.ENGLISH
import com.ibm.watson.language_translator.v3.util.Language.RUSSIAN
import kotlinx.android.synthetic.main.fragment_lyrics.buttonTranslate
import kotlinx.android.synthetic.main.fragment_lyrics.progressBar
import kotlinx.android.synthetic.main.fragment_lyrics.recyclerViewLyrics
import kotlinx.android.synthetic.main.fragment_lyrics.viewLyricsFooter
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.coroutines.CoroutineContext

class LyricsFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {
    private val presenter by lazy { LyricsPresenter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lyrics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.applyToolbar()

        requireActivity().imageViewBack.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }
        }

        val name = arguments?.getString(NAME) ?: ""
        val artists = arguments?.getString(ARTISTS) ?: ""

        launch {
            buttonTranslate.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            val originalLyrics = arguments?.getString(ORIGINAL_LYRICS)
                ?: getOriginalLyrics(name, artists)

            if (originalLyrics == null)
                handleNoLyrics()
            else
                handleLyrics(originalLyrics, artists, name)
        }
    }

    private fun handleNoLyrics() {
        recyclerViewLyrics.adapter = LyricsAdapter(listOf(getString(R.string.no_lyrics)))
        progressBar.visibility = View.GONE
        viewLyricsFooter.visibility = View.GONE
    }

    private suspend fun handleLyrics(
        originalLyrics: String,
        artists: String,
        name: String
    ) {
        val identifiedLanguage = originalLyrics.identifyLanguage()
        val targetLanguage = RUSSIAN

        if (identifiedLanguage == targetLanguage)
            handleSameLanguage(originalLyrics)
        else
            handleDifferentLanguage(
                originalLyrics,
                identifiedLanguage,
                targetLanguage,
                artists,
                name
            )
    }

    private fun handleSameLanguage(originalLyrics: String) {
        progressBar.visibility = View.GONE
        viewLyricsFooter.visibility = View.GONE
        recyclerViewLyrics.adapter = LyricsAdapter(originalLyrics.split('\n'))
    }

    private suspend fun handleDifferentLanguage(
        originalLyrics: String,
        identifiedLanguage: String,
        targetLanguage: String,
        artists: String,
        name: String
    ) {
        progressBar.visibility = View.GONE
        buttonTranslate.visibility = View.VISIBLE
        val isLyricsTranslated = arguments?.getBoolean(IS_LYRICS_TRANSLATED) ?: false
        if (!isLyricsTranslated)
            handleOriginalLyrics(originalLyrics)
        else
            handleTranslatedLyrics(originalLyrics, identifiedLanguage, targetLanguage)

        buttonTranslate.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.frameLayoutPlayer,
                LyricsFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARTISTS, artists)
                        putBoolean(IS_LYRICS_TRANSLATED, !isLyricsTranslated)
                        putString(NAME, name)
                        if (isLyricsTranslated) putString(ORIGINAL_LYRICS, originalLyrics)
                    }
                }
            ).commit()
        }
    }

    private fun handleOriginalLyrics(originalLyrics: String) {
        buttonTranslate.text = getString(R.string.translate)
        recyclerViewLyrics.adapter = LyricsAdapter(originalLyrics.split('\n'))
    }

    private suspend fun handleTranslatedLyrics(
        originalLyrics: String,
        identifiedLanguage: String,
        targetLanguage: String
    ) {
        val appLocale = Locale(ENGLISH)
        var languageNotIdentified = false
        val translatedLyrics: String
        val translationResult = originalLyrics.translateFromTo(identifiedLanguage, targetLanguage)
        if (translationResult != null)
            translatedLyrics = translationResult
        else {
            languageNotIdentified = true
            translatedLyrics = getString(R.string.unidentifiable_language)
        }
        buttonTranslate.text = getString(
            R.string.detected_language,
            if (languageNotIdentified)
                getString(R.string.elvish) // Language kinda identified, yet badly
            else
                Locale(identifiedLanguage).getDisplayLanguage(appLocale),
            Locale(targetLanguage).getDisplayLanguage(appLocale)
        )
        recyclerViewLyrics.adapter = LyricsAdapter(translatedLyrics.split('\n'))
    }
}
