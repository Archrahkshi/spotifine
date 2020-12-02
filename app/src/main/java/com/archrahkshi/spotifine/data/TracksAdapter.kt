package com.archrahkshi.spotifine.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.R
import kotlinx.android.synthetic.main.item_track.view.layoutItemList
import kotlinx.android.synthetic.main.item_track.view.textViewTrackArtist
import kotlinx.android.synthetic.main.item_track.view.textViewTrackDuration
import kotlinx.android.synthetic.main.item_track.view.textViewTrackName
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

@ExperimentalTime
class TracksAdapter(
    private val tracks: List<Track>,
    private val clickListener: (Track) -> Unit
) : RecyclerView.Adapter<TracksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
    )

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tracks[position], clickListener)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewTrackName = view.textViewTrackName
        private val textViewTrackArtist = view.textViewTrackArtist
        private val textViewTrackDuration = view.textViewTrackDuration
        private val layoutItemList = view.layoutItemList

        fun bind(track: Track, clickListener: (Track) -> Unit) {
            textViewTrackName.text = track.name
            textViewTrackArtist.text = track.artist
//            textViewTrackDuration.text = SimpleDateFormat("HH:mm:ss").format(track.duration)
            textViewTrackDuration.text = track.duration.milliseconds.toComponents { HH, mm, ss, _ ->
                var duration = ""
                if (HH > 0) duration += "$HH:"
                if (duration.isNotEmpty() && mm <= ONE_DIGIT) duration += '0'
                duration += "$mm:"
                if (duration.isNotEmpty() && ss <= ONE_DIGIT) duration += '0'
                "$duration$ss"
            }
            layoutItemList.setOnClickListener { clickListener(track) }
        }
    }
}
