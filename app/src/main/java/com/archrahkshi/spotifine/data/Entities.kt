package com.archrahkshi.spotifine.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val image: String,
    val name: String,
    val size: Int,
    val url: String,
)

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val image: String,
    val name: String,
    val url: String,
)

@Entity
data class Album(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val artists: String,
    val image: String,
    val name: String,
    val size: Int,
    val url: String,
)

@Entity
data class Track(
    @PrimaryKey(autoGenerate = true)
    var id: String,
    val artist: String,
    val duration: Long,
    val name: String,
)
