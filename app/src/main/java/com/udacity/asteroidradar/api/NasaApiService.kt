package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfToday
import com.udacity.asteroidradar.OfflineConstant
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofitForNeos = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL + Constants.ASTEROID_LIST_FEED_URL)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofitForToday = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

// Coroutine list of Asteroid items + Image of the Day
interface NasaApiService {

    @GET("feed")
    suspend fun getAsteroids(@Query(Constants.ASTEROID_LIST_FROM) fromDate: String,
                             @Query(Constants.ASTEROID_LIST_TO) toDate: String,
                             @Query(Constants.ASTEROID_LIST_KEY) key: String): String

    @GET(Constants.IMAGE_OF_TODAY + OfflineConstant.API_KEY)
    suspend fun getImageOfToday(): ImageOfToday

}

object NasaApi {
    val retrofitServiceForNeos : NasaApiService by lazy { retrofitForNeos.create(NasaApiService::class.java) }
    val retrofitServiceForTodaysImage : NasaApiService by lazy { retrofitForToday.create(NasaApiService::class.java) }
}