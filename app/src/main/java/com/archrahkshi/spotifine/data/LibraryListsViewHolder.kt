package com.archrahkshi.spotifine.data

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_library_list.view.*

class LibraryListsViewHolder<ListType>(view: View) : RecyclerView.ViewHolder(view) {
    private val textViewListName = view.textViewListName
    private val textViewListInfo = view.textViewListInfo
    private val layoutItemList = view.layoutLibraryList

    fun bind(listType: ListType, clickListener: (ListType) -> Unit) {
        textViewListName.text = when (listType) {
            is Playlist -> listType.name
            is Artist -> listType.name
            is Album -> listType.name
            else -> ""
        }
        textViewListInfo.text = when (listType) {
            is Playlist -> listType.size.toString() // TODO: поменять
            is Artist -> ""
            is Album -> listType.artist
            else -> ""
        }
        layoutItemList.setOnClickListener { clickListener(listType) }
    }
}