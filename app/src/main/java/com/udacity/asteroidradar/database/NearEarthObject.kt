package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "neo_table")
data class NearEarthObject(

//    @PrimaryKey(autoGenerate = true)
//    var neoID: Int = 0,

    @PrimaryKey
    val neoID: Long,

    @ColumnInfo(name = "code_name")
    var codeName: String = "",

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_farth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "potentially_hazardous")
    val isPotentiallyHazardous: Boolean

)
