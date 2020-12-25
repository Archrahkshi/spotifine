package com.archrahkshi.spotifine.ui.library.tracksFragment

import android.content.Intent
import com.archrahkshi.spotifine.R
import com.archrahkshi.spotifine.data.TracksAdapter
import com.archrahkshi.spotifine.ui.commonViews.ToolBarImpl
import com.archrahkshi.spotifine.ui.library.views.TracksHeaderImpl
import com.archrahkshi.spotifine.ui.library.views.TracksRecyclerImpl
import com.archrahkshi.spotifine.ui.player.PlayerActivity
import com.archrahkshi.spotifine.util.*
import kotlinx.android.synthetic.main.fragment_tracks.*
import kotlinx.android.synthetic.main.toolbar.*
import timber.log.Timber
import kotlin.time.ExperimentalTime

internal class TracksPresenter(private val fragment: TracksFragment) {
    private val activity = fragment.requireActivity()
    private val args = fragment.requireArguments()

    private val toolBarImpl
            by lazy { ToolBarImpl(activity.tvTitle, activity.imgBack) }

    private fun showBackButton() = toolBarImpl.applyBackButton(false)
    private fun hideBackButton() = toolBarImpl.applyBackButton(true)
    private fun setToolbarTitle(title: String) = toolBarImpl.setTitle(title)

    private val tracksHeaderImpl
            by lazy {
                TracksHeaderImpl(activity.textViewHeaderLine1,
                        activity.textViewHeaderLine2,
                        activity.textViewHeaderLine3,
                        activity.imageViewHeader)
            }

    private fun setHeaderText(text: String) = tracksHeaderImpl.setText(text)
    private fun setHeaderSubtext(subtext: String?) = tracksHeaderImpl.setSubtext(subtext)
    private fun setHeaderAdditionalText(additionalText: String) = tracksHeaderImpl.setAdditionalText(additionalText)
    private fun setHeaderImage(uri: String) = tracksHeaderImpl.setImage(uri)

    private val tracksRecyclerImpl
            by lazy { TracksRecyclerImpl(fragment.recyclerViewTracks) }

    @ExperimentalTime
    suspend fun applyRecycler() {
        tracksRecyclerImpl.setupRecycler(
            TracksAdapter(createTrackLists(args.getString(URL)!!, args.getString(ACCESS_TOKEN))) {
                Timber.tag("Track clicked").i(it.toString())
                fragment.requireContext().startActivity(
                    Intent(activity, PlayerActivity::class.java).apply {
                        putExtra(ID, it.id)
                        putExtra(DURATION, it.duration)
                        putExtra(NAME, it.name)
                        putExtra(ARTISTS, it.artists)
                    }
                )
        })
    }

    fun applyToolbar() {
        setToolbarTitle(fragment.getString(R.string.title_tracks))
        showBackButton()
    }

    fun applyHeader() {
        with(args) {
            setHeaderText(getString(NAME)!!)
            setHeaderSubtext(getString(ARTISTS))
            setHeaderAdditionalText(fragment.getString(R.string.header_line3, getInt(SIZE),
                    setWordTracks(fragment.requireContext(), getInt(SIZE))))
            setHeaderImage(getString(IMAGE)!!)
        }
    }
}