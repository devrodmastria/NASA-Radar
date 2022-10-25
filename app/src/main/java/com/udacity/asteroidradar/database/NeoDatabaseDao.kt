package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NeoDatabaseDao {

    @Insert
    suspend fun insertOneAsteroid(neoAsteroid: NearEarthObject)

    @Query("SELECT * from neo_table WHERE close_approach_date = :date")
    suspend fun get(date: String): List<NearEarthObject>?

//    @Query("SELECT * from neo_table WHERE neoID = :key")
//    suspend fun get(key: Long): NearEarthObject?

//    @Query("SELECT * FROM neo_table ORDER BY closeApproachDate DESC")
//    fun getAllNights(): LiveData<List<NearEarthObject>>

}