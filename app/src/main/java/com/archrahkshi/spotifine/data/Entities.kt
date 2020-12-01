package com.archrahkshi.spotifine.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: при необходимости - добавить поля

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
    val image: String,
    val name: String,
    val artists: String,
    val url: String,
)

@Entity
data class Track(
    @PrimaryKey(autoGenerate = true)
    var id: String,
    val name: String,
    val artist: String,
    val duration: Long,
)
