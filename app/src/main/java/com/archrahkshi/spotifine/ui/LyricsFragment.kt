package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.LyricsAdapter
import kotlinx.android.synthetic.main.fragment_lyrics.buttonTranslate
import kotlinx.android.synthetic.main.fragment_lyrics.recyclerViewLyrics

class LyricsFragment(private var isLyricsTranslated: Boolean) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lyrics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lyricsOriginal = listOf(
            "Hoort u mij toe",
            "Als ik u verhaal",
            "Van oude sagen",
            "Van reuzentijd",
            "",
            "Vertel ons de sagen",
            "Van oeroude machten",
            "In 't Gelderse land",
            "Wat weet u nog meer?",
        )
        val lyricsTranslated = listOf(
            "Послушай меня",
            "Если я скажу тебе",
            "Из старых саг",
            "Из гигантских времен",
            "",
            "Расскажи нам саги",
            "Древних сил",
            "В стране Гелдерланд",
            "Что еще ты знаешь?",
        )
        val lyrics = if (isLyricsTranslated) lyricsTranslated else lyricsOriginal

        val detectedLanguage = "..." // TODO
        val toRussian = "$detectedLanguage > ${resources.getString(R.string.russian)}"
        buttonTranslate.text =
            if (isLyricsTranslated)
                toRussian
            else
                resources.getString(R.string.translate)

        buttonTranslate.setOnClickListener {
            isLyricsTranslated = !isLyricsTranslated
            fragmentManager?.beginTransaction()?.replace(
                R.id.frameLayoutPlayer,
                LyricsFragment(isLyricsTranslated)
            )?.commit()
        }

        recyclerViewLyrics.adapter = LyricsAdapter(lyrics)
    }
}
