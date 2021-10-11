package com.example.mastermusic.model.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Data access objects (Dao)
@Dao
interface MusicDao {

    @Query("SELECT * from musics")
    fun getAll(): List<MusicEntityDb>

    @Insert
    suspend fun insertMusics(musics: List<MusicEntityDb>)

    @Query("DELETE from musics")
    suspend fun deleteAll()
}