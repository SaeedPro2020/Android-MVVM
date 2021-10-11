package com.example.mastermusic.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.mastermusic.model.data.MusicEntityDb
import com.example.mastermusic.model.data.Repository

class MyViewModel(app: Application) : AndroidViewModel(app) {

    private val dataSourceImpl = Repository(app)
    val musicData = dataSourceImpl.musicData

    val selectedMusic = MutableLiveData<MusicEntityDb>()

    fun refreshData() {
        dataSourceImpl.refreshDataFromWeb()
    }

}