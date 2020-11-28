package com.archrahkshi.spotifine.data

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_track.view.*
import java.text.SimpleDateFormat

class TracksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textViewTrackName = view.textViewTrackName
    private val textViewTrackArtist = view.textViewTrackArtist
    private val textViewTrackDuration = view.textViewTrackDuration
    private val layoutItemList = view.layoutItemList

    fun bind(track: Track, clickListener: (Track) -> Unit) {
        textViewTrackName.text = track.name
        textViewTrackArtist.text = track.artist
        textViewTrackDuration.text = SimpleDateFormat("HH:mm").format(track.duration)
        layoutItemList.setOnClickListener { clickListener(track) }
    }
}