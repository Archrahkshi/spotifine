package com.archrahkshi.spotifine.data

sealed class ListType

data class Playlist(
    val image: String,
    val name: String,
    val size: Int,
    val url: String,
) : ListType()

data class Artist(
    val image: String,
    val name: String,
    val url: String,
) : ListType()

data class Album(
    val artists: String,
    val image: String,
    val name: String,
    val size: Int,
    val url: String,
) : ListType()

data class Track(
    var id: String,
    val artists: String,
    val duration: Long,
    val name: String,
)
