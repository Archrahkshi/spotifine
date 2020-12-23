package com.archrahkshi.spotifine.ui.player.lyricsFragment

import android.app.Instrumentation
import android.os.Bundle
import android.util.Log
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
import com.ibm.watson.language_translator.v3.util.Language.RUSSIAN
import kotlinx.android.synthetic.main.fragment_lyrics.*
import kotlinx.android.synthetic.main.toolbar.*
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

        requireActivity().imgBack.setOnClickListener {

            CoroutineScope(Dispatchers.Default).launch {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK)
            }
        }

        val appLanguage = RUSSIAN
        val appLocale = Locale(appLanguage)

        val isLyricsTranslated = arguments?.getBoolean(IS_LYRICS_TRANSLATED) ?: false
        val name = arguments?.getString(NAME) ?: ""
        val artists = arguments?.getString(ARTISTS) ?: ""

        launch {

            progressBar.visibility = View.VISIBLE
            buttonTranslate.visibility = View.GONE
            Log.i("DD", "START")
            val originalLyrics = arguments?.getString(ORIGINAL_LYRICS)
                ?: getOriginalLyrics(name, artists)
            Log.i("DD", "FINISH")

            if (originalLyrics == null) {
                recyclerViewLyrics.adapter = LyricsAdapter(listOf(getString(R.string.no_lyrics)))
                progressBar.visibility = View.INVISIBLE
                buttonTranslate.visibility = View.GONE
                viewLyricsFloor.visibility = View.GONE
            } else {
                val identifiedLanguage = originalLyrics.identifyLanguage()

                if (identifiedLanguage == appLanguage) {
                    buttonTranslate.visibility = View.GONE
                    viewLyricsFloor.visibility = View.GONE
                    recyclerViewLyrics.adapter = LyricsAdapter(
                        originalLyrics.split('\n')
                    )
                    progressBar.visibility = View.INVISIBLE
                } else {
                    if (!isLyricsTranslated) {
                        try {
                            buttonTranslate.visibility = View.VISIBLE
                            buttonTranslate.text = getString(R.string.translate)
                            recyclerViewLyrics.adapter = LyricsAdapter(
                                    originalLyrics.split('\n')
                            )
                            progressBar.visibility = View.INVISIBLE
                        } catch(e: Exception) {e.printStackTrace()}
                    } else {
                        val translatedLyrics =
                            originalLyrics.translateFromTo(identifiedLanguage, appLanguage)
                                ?: getString(R.string.unidentifiable_language)
                        buttonTranslate.visibility = View.VISIBLE
                        try {
                            buttonTranslate.text = getString(
                                R.string.detected_language,
                                Locale(identifiedLanguage).getDisplayLanguage(appLocale),
                                appLocale.getDisplayLanguage(appLocale)
                            )
                        } catch (e: NullPointerException) { // Couldn't identify language
                            buttonTranslate.text = getString(
                                R.string.detected_language,
                                getString(R.string.elvish),
                                appLocale.getDisplayLanguage(appLocale)
                            )
                        }
                        recyclerViewLyrics.adapter = LyricsAdapter(
                            translatedLyrics.split('\n')
                        )
                        progressBar.visibility = View.INVISIBLE
                    }

                    try {
                        buttonTranslate.setOnClickListener {
                            fragmentManager?.beginTransaction()?.replace(
                                    R.id.frameLayoutPlayer,
                                    LyricsFragment().apply {
                                        arguments = Bundle().apply {
                                            putString(ARTISTS, artists)
                                            putBoolean(IS_LYRICS_TRANSLATED, !isLyricsTranslated)
                                            putString(NAME, name)
                                            if (isLyricsTranslated)
                                                putString(ORIGINAL_LYRICS, originalLyrics)
                                        }
                                    }
                            )?.commit()
                        }
                    } catch(e: Exception) {}
                }
            }
        }
    }
}
