package com.example.mastermusic.model.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.mastermusic.model.utilities.MYWEB_SERVICE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class Repository(val app: Application) {

    val musicData = MutableLiveData<List<MusicEntityDb>>()
    private val musicDao = MusicDatabase.getDatabase(app).musicDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val data = musicDao.getAll()
            if (data.isEmpty()){
                refreshDataFromWeb()
            }else{
                musicData.postValue(data)
            }
        }
    }
    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable(app)) {
            val retrofit = Retrofit.Builder()
                .baseUrl(MYWEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(Service::class.java)
            val serviceData = service.getMusicData().body() ?: emptyList()
            musicData.postValue(serviceData)
            musicDao.deleteAll()
            musicDao.insertMusics(serviceData)
        }
    }

    private fun networkAvailable(context: Context) : Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        } else {
            return false
        }
        capabilities.also {
            if (it != null){
                if (it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true
                else if (it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return true
                }
            }
        }
        return false
    }

    fun refreshDataFromWeb() {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }
}