package com.archrahkshi.spotifine.ui.library.tracksFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.Track
import com.archrahkshi.spotifine.ui.adapters.TracksAdapter
import com.archrahkshi.spotifine.data.factories.TracksListProviderFactory
import com.archrahkshi.spotifine.ui.commonViews.IToolbar
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.ITracksHeader
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.ITracksList
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters.ToolbarPresenter
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters.TracksHeaderPresenter
import com.archrahkshi.spotifine.ui.library.tracksFragment.views.presenters.TracksListPresenter
import com.archrahkshi.spotifine.ui.player.PlayerActivity
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.DURATION
import com.archrahkshi.spotifine.util.ID
import com.archrahkshi.spotifine.util.IMAGE
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SIZE
import com.archrahkshi.spotifine.util.URL
import com.archrahkshi.spotifine.util.setWordTracks
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_tracks.imageViewHeader
import kotlinx.android.synthetic.main.fragment_tracks.recyclerViewTracks
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine1
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine2
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine3
import kotlinx.android.synthetic.main.toolbar.btnBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

class TracksFragment(
    override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope, ITracksHeader, ITracksList, IToolbar {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)

    private val tracksListPresenter by lazy { TracksListPresenter(this) }
    private val tracksHeaderPresenter by lazy { TracksHeaderPresenter(this) }
    private val toolbarPresenter by lazy { ToolbarPresenter(this) }

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TracksListProviderFactory.provide()

        //
        tracksListPresenter.setupList(
            requireArguments().getString(URL)!!,
            requireArguments().getString(ACCESS_TOKEN)
        )
        //
        toolbarPresenter.setupToolbar()
        //
        tracksHeaderPresenter.setText(requireArguments().getString(NAME)!!)
        tracksHeaderPresenter.setSubtext(requireArguments().getString(ARTISTS))
        tracksHeaderPresenter.setAdditionalText(
            getString(
                R.string.header_line3,
                requireArguments().getInt(SIZE),
                setWordTracks(context, requireArguments().getInt(SIZE))
            )
        )
        tracksHeaderPresenter.setImage(requireArguments().getString(IMAGE)!!)
    }

    /**
     * TracksHeader implementation
     */
    override fun setText(text: String) {
        textViewHeaderLine1.text = text
    }

    override fun setSubtext(text: String?) {
        if (text != null)
            textViewHeaderLine2.text = text
        else
            textViewHeaderLine2.visibility = View.GONE
    }

    override fun setAdditionalText(text: String) {
        textViewHeaderLine3.text = text
    }

    override fun setImage(uri: String) {
        Glide.with(this).load(uri).into(imageViewHeader)
    }

    /**
     * TracksList implementation
     */

    @ExperimentalTime
    override suspend fun setupList(list: List<Track>) {
        withContext(Main) {
            recyclerViewTracks.adapter = TracksAdapter(list) {
                Timber.i(it.toString())
                requireContext().startActivity(
                    Intent(activity, PlayerActivity::class.java).apply {
                        putExtra(ID, it.id)
                        putExtra(DURATION, it.duration)
                        putExtra(NAME, it.name)
                        putExtra(ARTISTS, it.artists)
                    }
                )
            }
        }
    }

    /**
     * Toolbar implementation
     */

    override fun setTitle(title: String) {
        requireActivity().textViewToolbarText.text = title
    }

    override fun showBackButton(isShown: Boolean) {
        requireActivity().btnBack.visibility = if (isShown) View.VISIBLE else View.INVISIBLE
    }
}
