package com.archrahkshi.spotifine.data

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.R

class LibraryListsAdapter<ListType>(
    private val libraryLists: List<ListType>,
    private val clickListener: (ListType) -> Unit
) : RecyclerView.Adapter<LibraryListsViewHolder<ListType>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = LibraryListsViewHolder<ListType>(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_library_list, parent, false)
    )

    override fun getItemCount() = libraryLists.size

    override fun onBindViewHolder(holder: LibraryListsViewHolder<ListType>, position: Int) {
        holder.bind(libraryLists[position], clickListener)
    }
}