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
import com.archrahkshi.spotifine.data.factories.TrackDataProviderFactory
import com.archrahkshi.spotifine.ui.adapters.LyricsAdapter
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
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
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.imageViewSettings
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
            with(requireArguments()) {
                lyricsPresenter.load(
                    getString(ORIGINAL_LYRICS) ?: getOriginalLyrics(
                        getString(NAME)!!,
                        getString(ARTISTS)!!
                    )
                )
            }
        }

        requireActivity().imageViewBack.setOnClickListener {
            CoroutineScope(Dispatchers.Default).launch {
                Instrumentation().sendKeyDownUpSync(KEYCODE_BACK)
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
        requireActivity().imageViewBack.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    override fun hideSettingsButton() {
        requireActivity().imageViewSettings.visibility = View.GONE
    }

    /**
     * Lyrics implementation
     */

    override fun applyButtonTranslate(originalLyrics: String) {
        try {
            buttonTranslate.setOnClickListener {
                val args = arguments
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.frameLayoutPlayer,
                    LyricsFragment().apply {
                        arguments = Bundle().apply {
                            putString(
                                ARTISTS,
                                TrackDataProviderFactory.instance!!.getArtists()
                            )
                            putBoolean(
                                IS_LYRICS_TRANSLATED,
                                !(args?.getBoolean(IS_LYRICS_TRANSLATED) ?: false)
                            )
                            putString(
                                NAME,
                                TrackDataProviderFactory.instance!!.getName()
                            )
                            if (args?.getBoolean(IS_LYRICS_TRANSLATED) == true)
                                putString(ORIGINAL_LYRICS, originalLyrics)
                        }
                    }
                ).commit()
            }
        } catch (e: NullPointerException) {
            // Important to prevent crash
        }
    }

    override fun setupLyrics(lyrics: List<String>, buttonText: String?) {
        recyclerViewLyrics.adapter = LyricsAdapter(lyrics)
        buttonText?.let {
            buttonTranslate.text = it
        }
    }

    override fun loading() {
        buttonTranslate.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    override fun loaded(successfully: Boolean) {
        buttonTranslate.visibility = if (successfully) View.VISIBLE else View.GONE
        progressBar.visibility = View.GONE
    }
}
