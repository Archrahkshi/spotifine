package com.archrahkshi.spotifine.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Track
import com.archrahkshi.spotifine.data.TracksAdapter
import com.archrahkshi.spotifine.data.URL
import kotlinx.android.synthetic.main.fragment_tracks.*

class TracksFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        val url = this.arguments?.getString(URL)
        val tracks = listOf<Track>(
            Track(id = 0, name = "Sample Track 1", "Sample Artist 1", 10000, "sample_url_1"),
            Track(id = 1, name = "Sample Track 2", "Sample Artist 2", 20000, "sample_url_2"),
            Track(id = 2, name = "Sample Track 3", "Sample Artist 3", 30000, "sample_url_3")
        ) // TODO: URL-запрос и вывод конкретного списка треков по url
    
        recyclerViewTracks.adapter = TracksAdapter(tracks) {
            Log.i("Track", it.toString())
            startActivity(Intent(activity, PlayerActivity::class.java))
        }
    }
}