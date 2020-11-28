package com.archrahkshi.spotifine.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.*
import kotlinx.android.synthetic.main.fragment_library_lists.*

class LibraryListsFragment<ListType> : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val libraryLists = listOf<ListType>() // TODO

        recyclerViewLists.adapter = LibraryListsAdapter(libraryLists) {
            fragmentManager?.beginTransaction()?.replace(
                R.id.frameLayoutLibrary,
                when (it) {
                    is Playlist -> TracksFragment().apply {
                        arguments = Bundle().apply {
                            putString(URL, it.url)
                        }
                    }
                    is Artist -> LibraryListsFragment<Album>().apply {
                        arguments = Bundle().apply {
                            putString(URL, it.url)
                        }
                    }
                    is Album -> TracksFragment().apply {
                        arguments = Bundle().apply {
                            putString(URL, it.url)
                        }
                    }
                    else -> null // TODO: разобраться с sealed классами и убрать этот костыль
                }!!
            )?.addToBackStack(null)?.commit()
        }
    }
}