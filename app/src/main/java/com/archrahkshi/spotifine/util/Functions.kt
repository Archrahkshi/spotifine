package com.archrahkshi.spotifine.util

import android.content.Context
import com.archrahkshi.spotifine.R
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

fun setWordTracks(context: Context?, size: Int?) = context?.resources?.getString(
    with(size.toString()) {
        when {
            endsWith('1') -> R.string.tracks_singular
            endsWith('2') || endsWith('3') || endsWith('4') ->
                R.string.tracks_paucal
            else -> R.string.tracks_plural
        }
    }
)

@ExperimentalTime
fun formatDuration(milliseconds: Long) = milliseconds.milliseconds.toComponents { HH, mm, ss, _ ->
    var duration = ""
    if (HH > 0) duration += "$HH:"
    if (duration.isNotEmpty() && mm <= ONE_DIGIT) duration += '0'
    duration += "$mm:"
    if (duration.isNotEmpty() && ss <= ONE_DIGIT) duration += '0'
    "$duration$ss"
}