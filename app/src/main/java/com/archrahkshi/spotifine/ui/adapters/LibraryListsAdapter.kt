package com.archrahkshi.spotifine.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.ListType
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.util.setWordTracks
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_library_list.view.imageViewListPic
import kotlinx.android.synthetic.main.item_library_list.view.layoutLibraryList
import kotlinx.android.synthetic.main.item_library_list.view.textViewListInfo
import kotlinx.android.synthetic.main.item_library_list.view.textViewListName

class LibraryListsAdapter(
    private val libraryLists: List<ListType>,
    private val clickListener: (ListType, Int) -> Unit
) : ListAdapter<ListType, LibraryListsAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ListType>() {
        override fun areItemsTheSame(oldItem: ListType, newItem: ListType) = oldItem == newItem
        override fun areContentsTheSame(oldItem: ListType, newItem: ListType) = oldItem == newItem
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_library_list, parent, false)
    )

    override fun getItemCount() = libraryLists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(libraryLists[position], clickListener, position)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewListPic = view.imageViewListPic
        private val layoutItemList = view.layoutLibraryList
        private val textViewListInfo = view.textViewListInfo
        private val textViewListName = view.textViewListName
        private val context = view.context
        private val viewTest = view

        fun bind(listType: ListType, clickListener: (ListType, Int) -> Unit, position: Int) {
            textViewListName.text = when (listType) {
                is Playlist -> listType.name
                is Artist -> listType.name
                is Album -> listType.name
            }
            textViewListInfo.text = when (listType) {
                is Playlist -> {
                    val size = listType.size
                    "$size ${setWordTracks(context, size)}"
                }
                is Artist -> ""
                is Album -> listType.artists
            }
            Glide.with(viewTest).load(
                when (listType) {
                    is Playlist -> listType.image
                    is Artist -> listType.image
                    is Album -> listType.image
                }
            ).into(imageViewListPic)
            layoutItemList.setOnClickListener { clickListener(listType, position) }
        }
    }
}
