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
import retrofit2.http.Path
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

// Coroutine list of Asteroid items + Image of the Day
interface NasaApiService {

    @GET(Constants.IMAGE_OF_TODAY + OfflineConstant.API_KEY)
    suspend fun getImageOfToday(): ImageOfToday

    // ******* com.squareup.moshi.JsonDataException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at path $
    // https://github.com/square/moshi
    @GET(Constants.ASTEROID_LIST_FEED)
    suspend fun getAsteroids(@Query(Constants.ASTEROID_LIST_FROM) fromDate: String,
                             @Query(Constants.ASTEROID_LIST_TO) toDate: String,
                             @Query(Constants.ASTEROID_LIST_KEY) key: String): List<Asteroid>
}

object NasaApi {
    val retrofitService : NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }
}