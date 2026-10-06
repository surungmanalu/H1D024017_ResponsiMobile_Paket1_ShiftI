package com.example.myapplication.data.remote

import com.example.myapplication.data.model.BmkgResponse
import retrofit2.http.GET

/**
 * Retrofit API Service untuk portal data terbuka BMKG.
 */
interface BmkgApiService {
    @GET("DataMKG/TEWS/gempaterkini.json")
    suspend fun getGempaTerkini(): BmkgResponse
}
