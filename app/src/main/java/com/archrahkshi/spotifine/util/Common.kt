package com.archrahkshi.spotifine.util

import android.content.Context
import com.archrahkshi.spotifine.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber
import java.io.IOException
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
fun Long.format() = this.milliseconds.toComponents { HH, mm, ss, _ ->
    var duration = ""
    if (HH > 0) duration += "$HH:"
    if (duration.isNotEmpty() && mm <= ONE_DIGIT) duration += '0'
    duration += "$mm:"
    if (duration.isNotEmpty() && ss <= ONE_DIGIT) duration += '0'
    "$duration$ss"
}

fun String.getJson(accessToken: String?): JsonObject = JsonParser().parse(
    try {
        this.buildRequest(accessToken)
    } catch (e: IOException) {
        Timber.wtf(e)
        null
    }
).asJsonObject

fun String.buildRequest(accessToken: String?) = OkHttpClient().newCall(
    Request.Builder()
        .url(this)
        .header("Authorization", "Bearer $accessToken")
        .header("Accept", "application/json")
        .header("Content-Type", "application/json")
        .build()
        .also { Timber.i(it.toString()) }
).execute().body?.string()
