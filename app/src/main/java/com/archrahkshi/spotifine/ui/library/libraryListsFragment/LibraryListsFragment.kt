package com.archrahkshi.spotifine.ui.library.libraryListsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Album
import com.archrahkshi.spotifine.data.Artist
import com.archrahkshi.spotifine.data.ListType
import com.archrahkshi.spotifine.data.Playlist
import com.archrahkshi.spotifine.data.factories.LibraryListProviderFactory
import com.archrahkshi.spotifine.data.factories.TrackDataProviderFactory
import com.archrahkshi.spotifine.ui.adapters.LibraryListsAdapter
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.ITracksList
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters.LibraryListPresenter
import com.archrahkshi.spotifine.ui.library.libraryListsFragment.views.presenters.ToolbarPresenter
import com.archrahkshi.spotifine.ui.library.tracksFragment.TracksFragment
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ALBUMS
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.IMAGE
import com.archrahkshi.spotifine.util.LIST_TYPE
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SIZE
import com.archrahkshi.spotifine.util.URL
import kotlinx.android.synthetic.main.fragment_library_lists.recyclerViewLists
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class LibraryListsFragment(
    override val coroutineContext: CoroutineContext = Main.immediate
) : Fragment(), CoroutineScope, ITracksList, IToolbar {
    private val toolbarPresenter by lazy { ToolbarPresenter(this) }
    private val libraryListPresenter by lazy { LibraryListPresenter(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_library_lists, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LibraryListProviderFactory.provide(this)

        toolbarPresenter.setupToolbar(
            requireArguments().getString(NAME),
            requireArguments().getString(LIST_TYPE) == ALBUMS
        )

        libraryListPresenter.setupList(
            arguments?.getString(URL) ?: "me",
            requireArguments().getString(LIST_TYPE)!!,
            TrackDataProviderFactory.instance!!.getAccessToken()
        )
    }

    /**
     * Toolbar implementation
     */

    override fun setTitle(title: String) {
        requireActivity().textViewToolbarText.text = title
    }

    override fun showBackButton(isShown: Boolean) {
        requireActivity().imageViewBack.visibility = if (isShown) View.VISIBLE else View.GONE
    }

    /**
     * TracksList implementation
     */

    override suspend fun setupList(list: List<ListType>) {
        withContext(Main) {
            try {
                recyclerViewLists.adapter = LibraryListsAdapter(list) { listType ->
                    requireActivity().supportFragmentManager.beginTransaction().replace(
                        R.id.frameLayoutLibrary,
                        when (listType) {
                            is Playlist -> TracksFragment().apply {
                                arguments = Bundle().apply {
                                    putString(
                                        ACCESS_TOKEN,
                                        TrackDataProviderFactory.instance!!.getAccessToken()
                                    )
                                    putString(IMAGE, listType.image)
                                    putString(NAME, listType.name)
                                    putInt(SIZE, listType.size)
                                    putString(URL, listType.url)
                                }
                            }
                            is Artist -> LibraryListsFragment().apply {
                                arguments = Bundle().apply {
                                    putString(
                                        ACCESS_TOKEN,
                                        TrackDataProviderFactory.instance!!.getAccessToken()
                                    )
                                    putString(IMAGE, listType.image)
                                    putString(NAME, listType.name)
                                    putString(LIST_TYPE, ALBUMS)
                                    putString(URL, listType.url)
                                }
                            }
                            is Album -> TracksFragment().apply {
                                arguments = Bundle().apply {
                                    putString(
                                        ACCESS_TOKEN,
                                        TrackDataProviderFactory.instance!!.getAccessToken()
                                    )
                                    putString(ARTISTS, listType.artists)
                                    putString(IMAGE, listType.image)
                                    putString(NAME, listType.name)
                                    putInt(SIZE, listType.size)
                                    putString(URL, listType.url)
                                }
                            }
                        }
                    ).setTransition(TRANSIT_FRAGMENT_FADE).addToBackStack(null).commit()
                }
            } catch (e: NullPointerException) {
            }
        }
    }
}
