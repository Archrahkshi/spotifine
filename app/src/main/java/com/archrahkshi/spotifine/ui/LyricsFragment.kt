package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import kotlinx.android.synthetic.main.fragment_lyrics.*


class LyricsFragment(private var isLyricsTranslated: Boolean) : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_lyrics, container, false)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val toRussian = "... > РУССКИЙ"
        buttonTranslate.text =
            if (isLyricsTranslated) toRussian else R.string.translate.toString() // TODO
        
        buttonTranslate.setOnClickListener {
            isLyricsTranslated = !isLyricsTranslated
            fragmentManager?.beginTransaction()?.replace(
                R.id.frameLayoutPlayer,
                LyricsFragment(isLyricsTranslated)
            )?.commit()
        }
    }
}