package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Track
import com.archrahkshi.spotifine.data.TracksAdapter
import com.archrahkshi.spotifine.data.URL
import kotlinx.android.synthetic.main.fragment_lyrics.*

class TracksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = this.arguments?.getString(URL)
        val tracks = listOf<Track>()// TODO: URL-запрос и вывод конкретного плейлиста/альбома по url

        recyclerView.adapter = TracksAdapter(tracks) {
            // TODO
        }
    }
}