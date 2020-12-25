package com.archrahkshi.spotifine.ui.library.views

import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.data.TracksAdapter
import kotlin.time.ExperimentalTime

interface ITracksRecycler {

    @ExperimentalTime
    fun setupRecycler(adapter: RecyclerView.Adapter<TracksAdapter.ViewHolder>)

}


class TracksRecyclerImpl(private val recycler: RecyclerView) : ITracksRecycler {

    @ExperimentalTime
    override fun setupRecycler(adapter: RecyclerView.Adapter<TracksAdapter.ViewHolder>) {

        recycler.adapter = adapter

    }
}