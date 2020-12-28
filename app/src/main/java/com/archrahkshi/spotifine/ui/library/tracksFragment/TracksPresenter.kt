package com.archrahkshi.spotifine.ui.library.tracksFragment

import android.content.Intent
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.TracksAdapter
import com.archrahkshi.spotifine.ui.commonViews.ToolbarImpl
import com.archrahkshi.spotifine.ui.library.views.TracksHeaderImpl
import com.archrahkshi.spotifine.ui.library.views.TracksRecyclerImpl
import com.archrahkshi.spotifine.ui.player.PlayerActivity
import com.archrahkshi.spotifine.util.ACCESS_TOKEN
import com.archrahkshi.spotifine.util.ARTISTS
import com.archrahkshi.spotifine.util.DURATION
import com.archrahkshi.spotifine.util.ID
import com.archrahkshi.spotifine.util.IMAGE
import com.archrahkshi.spotifine.util.NAME
import com.archrahkshi.spotifine.util.SIZE
import com.archrahkshi.spotifine.util.URL
import com.archrahkshi.spotifine.util.createTrackLists
import com.archrahkshi.spotifine.util.setWordTracks
import kotlinx.android.synthetic.main.fragment_tracks.imageViewHeader
import kotlinx.android.synthetic.main.fragment_tracks.recyclerViewTracks
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine1
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine2
import kotlinx.android.synthetic.main.fragment_tracks.textViewHeaderLine3
import kotlinx.android.synthetic.main.toolbar.imageViewBack
import kotlinx.android.synthetic.main.toolbar.textViewToolbarText
import timber.log.Timber
import kotlin.time.ExperimentalTime

internal class TracksPresenter(private val fragment: TracksFragment) {
    private val activity = fragment.requireActivity()
    private val args = fragment.requireArguments()
    private val context = fragment.requireContext()

    private val toolBarImpl by lazy {
        ToolbarImpl(
            activity.textViewToolbarText,
            activity.imageViewBack
        )
    }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    private val tracksHeaderImpl by lazy {
        TracksHeaderImpl(
            activity.textViewHeaderLine1,
            activity.textViewHeaderLine2,
            activity.textViewHeaderLine3,
            activity.imageViewHeader
        )
    }

    private fun setHeaderText(text: String) = tracksHeaderImpl.setText(text)
    private fun setHeaderSubtext(subtext: String?) = tracksHeaderImpl.setSubtext(subtext)
    private fun setHeaderAdditionalText(additionalText: String) =
        tracksHeaderImpl.setAdditionalText(additionalText)

    private fun setHeaderImage(uri: String) = tracksHeaderImpl.setImage(uri)

    private val tracksRecyclerImpl by lazy { TracksRecyclerImpl(fragment.recyclerViewTracks) }

    @ExperimentalTime
    suspend fun applyRecycler() {
        tracksRecyclerImpl.setupRecycler(
            TracksAdapter(createTrackLists(args.getString(URL)!!, args.getString(ACCESS_TOKEN))) {
                Timber.i(it.toString())
                context.startActivity(
                    Intent(activity, PlayerActivity::class.java).apply {
                        putExtra(ID, it.id)
                        putExtra(DURATION, it.duration)
                        putExtra(NAME, it.name)
                        putExtra(ARTISTS, it.artists)
                    }
                )
            }
        )
    }

    fun applyToolbar() {
        setToolbarTitle(fragment.getString(R.string.title_tracks))
        showBackButton()
    }

    fun applyHeader() {
        with(args) {
            setHeaderText(getString(NAME)!!)
            setHeaderSubtext(getString(ARTISTS))
            setHeaderAdditionalText(
                fragment.getString(
                    R.string.header_line3,
                    getInt(SIZE),
                    setWordTracks(context, getInt(SIZE))
                )
            )
            setHeaderImage(getString(IMAGE)!!)
        }
    }
}
