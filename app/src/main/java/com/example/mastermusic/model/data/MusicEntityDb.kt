package com.example.mastermusic.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musics")
data class MusicEntityDb(
    @PrimaryKey(autoGenerate = true)
    val musicId: Int,
    val thImageFile: String,
    val artWorkImage: String,
    val artistName: String,
    val trackName: String,
    val albumName: String,
    val releaseDate: String,
    val price: Double
)
