package com.archrahkshi.spotifine.ui.library.tracksFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.archrahkshi.spotifine.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.ExperimentalTime

class TracksFragment(
        override val coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : Fragment(), CoroutineScope {

    private val presenter by lazy { TracksPresenter(this) }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_tracks, container, false)

    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.applyHeader()
        presenter.applyToolbar()
        launch { presenter.applyRecycler() }
    }
}
