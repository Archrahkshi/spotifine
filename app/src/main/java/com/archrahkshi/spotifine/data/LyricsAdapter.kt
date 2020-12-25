package com.archrahkshi.spotifine.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.R
import kotlinx.android.synthetic.main.item_lyrics_line.view.*

class LyricsAdapter(
        private val lines: List<String>
) : ListAdapter<String, LyricsAdapter.ViewHolder>(object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_lyrics_line, parent, false)
    )

    override fun getItemCount() = lines.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(lines[position])


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(line: String) {
            itemView.textViewLyricsLine.text = line
        }
    }
}
