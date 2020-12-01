package com.archrahkshi.spotifine.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_library_list.view.*

class LibraryListsAdapter<ListType>(
    private val libraryLists: List<ListType>,
    private val clickListener: (ListType) -> Unit
) : RecyclerView.Adapter<LibraryListsAdapter.ViewHolder<ListType>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder<ListType>(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_library_list, parent, false)
    )

    override fun getItemCount() = libraryLists.size

    override fun onBindViewHolder(holder: ViewHolder<ListType>, position: Int) {
        holder.bind(libraryLists[position], clickListener)
    }

    class ViewHolder<ListType>(view: View) : RecyclerView.ViewHolder(view) {
        private val textViewListName = view.textViewListName
        private val textViewListInfo = view.textViewListInfo
        private val layoutItemList = view.layoutLibraryList
        private val imageViewListPic = view.imageViewListPic
        private val viewTest = view

        fun bind(listType: ListType, clickListener: (ListType) -> Unit) {
            textViewListName.text = when (listType) {
                is Playlist -> listType.name
                is Artist -> listType.name
                is Album -> listType.name
                else -> null
            }
            textViewListInfo.text = when (listType) {
                is Playlist -> listType.size.toString() // TODO: поменять
                is Artist -> ""
                is Album -> listType.artists
                else -> null
            }
            Glide.with(viewTest).load(
                when (listType) {
                    is Playlist -> listType.image
                    is Artist -> listType.image
                    is Album -> listType.image
                    else -> null
                }
            ).into(imageViewListPic)
            layoutItemList.setOnClickListener { clickListener(listType) }
        }
    }
}