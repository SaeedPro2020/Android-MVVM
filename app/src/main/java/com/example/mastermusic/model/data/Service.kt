package com.example.mastermusic.model.data

import com.example.mastermusic.model.utilities.REST_API
import retrofit2.Response
import retrofit2.http.GET

interface Service {
    @GET(REST_API)
    suspend fun getMusicData(): Response<List<MusicEntityDb>>
}