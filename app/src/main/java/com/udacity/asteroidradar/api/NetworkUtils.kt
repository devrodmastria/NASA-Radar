package com.udacity.asteroidradar.api

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JsonObject): ArrayList<Asteroid> {

    Log.i("-->> Nasa API", "parseAsteroidsJsonResult ")

    // backup from original template
//    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val jsonNEO = jsonResult.jsonObject.get("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {

        if (jsonNEO != null) {

//            Log.i("-->> Nasa API", "jsonNEO $formattedDate has array size ${jsonNEO.jsonObject.get(formattedDate)?.jsonArray?.size}")

            val asteroidsForThisDate = jsonNEO.jsonObject.get(formattedDate)?.jsonArray
            val asteroidCount = jsonNEO.jsonObject.get(formattedDate)?.jsonArray?.size

            for (asteroidItem in 0 until asteroidCount!!){
                val asteroidJson = asteroidsForThisDate?.get(asteroidItem)
//                Log.i("-->> Nasa API", "jsonNEO $formattedDate with item $asteroidItem known as ${asteroidJson?.jsonObject?.get("name")}")

                val id = asteroidJson?.jsonObject?.get("id").toString()
                val codename = asteroidJson?.jsonObject?.get("name").toString()
                val closeApproachData = asteroidJson?.jsonObject?.get("close_approach_data")?.jsonArray?.get(0)?.jsonObject

                val relativeVelocity = closeApproachData?.jsonObject?.get("relative_velocity")?.jsonObject?.get("kilometers_per_second")
//                Log.i("-->> Nasa API", "jsonNEO $codename $relativeVelocity KM/s")

                val distanceFromEarth = closeApproachData?.jsonObject?.get("miss_distance")?.jsonObject?.get("astronomical")
//                Log.i("-->> Nasa API", "jsonNEO $codename $distanceFromEarth astronomical")

                val isPotentiallyHazardous = asteroidJson?.jsonObject?.get("is_potentially_hazardous_asteroid")
//                Log.i("-->> Nasa API", "jsonNEO $codename $isPotentiallyHazardous hazard")

                val absoluteMagnitude = asteroidJson?.jsonObject?.get("absolute_magnitude_h")
//                Log.i("-->> Nasa API", "jsonNEO $codename $absoluteMagnitude absoluteMagnitude")

                val estimatedDiameter = asteroidJson?.jsonObject?.get("estimated_diameter")
                    ?.jsonObject?.get("kilometers")?.jsonObject?.get("estimated_diameter_max")
//                Log.i("-->> Nasa API", "jsonNEO $codename $estimatedDiameter estimatedDiameter")

                val asteroid = Asteroid(
                    id.toLong(),
                    codename,
                    formattedDate,
                    absoluteMagnitude.toString().toDouble(),
                    estimatedDiameter.toString().toDouble(),
                    relativeVelocity.toString().toDouble(),
                    distanceFromEarth.toString().toDouble(),
                    isPotentiallyHazardous.toString().toBoolean())

                asteroidList.add(asteroid)

            }
        }
    }

    // backup decoder from older JSONObject library
//    for (formattedDate in nextSevenDaysFormattedDates) {
//        if (nearEarthObjectsJson.has(formattedDate)) {
//            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)
//
//            for (i in 0 until dateAsteroidJsonArray.length()) {
//
//                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
//                val id = asteroidJson.getLong("id")
//                val codename = asteroidJson.getString("name")
//
//                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
//                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
//                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")
//
//                val closeApproachData = asteroidJson
//                    .getJSONArray("close_approach_data").getJSONObject(0)
//                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
//                    .getDouble("kilometers_per_second")
//                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
//                    .getDouble("astronomical")
//                val isPotentiallyHazardous = asteroidJson
//                    .getBoolean("is_potentially_hazardous_asteroid")
//
//                val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
//                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
//                asteroidList.add(asteroid)
//            }
//        }
//    }

    return asteroidList
}

// public function to fill in dates for querying NASA web service
fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}
