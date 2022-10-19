package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfToday
import com.udacity.asteroidradar.OfflineConstant
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL + Constants.IMAGE_OF_TODAY + OfflineConstant.Auth.API_KEY)
    .build()

// Coroutine list of Asteroid items
interface NasaApiService {

    @GET("url")
    suspend fun getImageOfToday(): ImageOfToday

    @GET("url")
    suspend fun getAsteroids(): List<Asteroid>
}

object NasaApi {
    val retrofitService : NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }
}