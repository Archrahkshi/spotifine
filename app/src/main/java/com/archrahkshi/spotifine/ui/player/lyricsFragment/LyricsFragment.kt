package com.archrahkshi.spotifine.ui.player.lyricsFragment

import android.app.Instrumentation
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_BACK
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.factories.LyricsProviderFactory
import com.archrahkshi.spotifine.ui.adapters.LyricsAdapter
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
import com.archrahkshi.spotifine.ui.player.PlayerActivity
import com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters.ILyrics
import com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters.LyricsPresenter
import com.archrahkshi.spotifine.ui.player.lyricsFragment.views.presenters.ToolbarPresenter
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.IS_LYRICS_TRANSLATED
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.ORIGINAL_LYRICS
import com.archrahkshi.spotifine.util.getOriginalLyrics
import kotlinx.android.synthetic.main.fragment_lyrics.buttonTranslate
import kotlinx.android.synthetic.main.fragment_lyrics.progressBar
import kotlinx.android.synthetic.main.fragment_lyrics.recyclerViewLyrics
import kotlinx.android.synthetic.main.toolbar.btnBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LyricsFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope, IToolbar, ILyrics {

    private val toolbarPresenter by lazy { ToolbarPresenter(this) }
    private val lyricsPresenter by lazy { LyricsPresenter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lyrics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LyricsProviderFactory.provide(this)

        toolbarPresenter.setupToolbar(requireArguments().getString(NAME)!!)
        launch {
            lyricsPresenter.load(
                requireArguments().getString(ORIGINAL_LYRICS) ?: getOriginalLyrics(
                    requireArguments().getString(NAME)!!,
                    requireArguments().getString(ARTISTS)!!
                )
            )
        }

        requireActivity().btnBack.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                val inst = Instrumentation()
                inst.sendKeyDownUpSync(KEYCODE_BACK)
            }
        }
    }

    /**
     * Toolbar implementation
     */

    override fun setTitle(title: String) {
        requireActivity().textViewToolbarText.text = title
    }

    override fun showBackButton(isShown: Boolean) {
        requireActivity().btnBack.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    /**
     * Lyrics implementation
     */

    override fun applyButtonTranslate(originalLyrics: String) {
        try {
            buttonTranslate.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutPlayer,
                    LyricsFragment().apply {
                        arguments = Bundle().apply {
                            putString(
                                ARTISTS,
                                arguments?.getString(ARTISTS) ?: PlayerActivity.artists
                            )
                            putBoolean(
                                IS_LYRICS_TRANSLATED,
                                !(arguments?.getBoolean(IS_LYRICS_TRANSLATED) ?: false)
                            )
                            putString(NAME, arguments?.getString(NAME) ?: PlayerActivity.name)
                            if (arguments?.getBoolean(IS_LYRICS_TRANSLATED) == true)
                                putString(ORIGINAL_LYRICS, originalLyrics)
                        }
                    }
                ).commit()
            }
        } catch (e: Exception) {}
    }

    override fun setupLyrics(lyrics: List<String>, buttonText: String?) {
        try {
            recyclerViewLyrics.adapter = LyricsAdapter(lyrics)
            buttonText?.let {
                buttonTranslate.text = it
            }
        } catch (e: NullPointerException) {
        }
    }

    override fun loading() {
        try {
            buttonTranslate.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } catch (e: NullPointerException) {
        }
    }

    override fun loaded() {
        try {
            buttonTranslate.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        } catch (e: NullPointerException) {
        }
    }
}
