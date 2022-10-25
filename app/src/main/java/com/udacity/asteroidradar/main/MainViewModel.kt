package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.ImageOfToday
import com.udacity.asteroidradar.OfflineConstant
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.NearEarthObject
import com.udacity.asteroidradar.database.NeoDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel(val database: NeoDatabaseDao) : ViewModel() {

    private val _status = MutableLiveData<NasaApiStatus>()
    val apiStatus : LiveData<NasaApiStatus>
        get() = _status

    private val _imageToday = MutableLiveData<ImageOfToday>()
    val imageOfToday: LiveData<ImageOfToday>
        get() = _imageToday

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _asteroidListLiveData = MutableLiveData<List<Asteroid>>()
    val asteroidLiveData : LiveData<List<Asteroid>>
        get() = _asteroidListLiveData

    init {

        downloadAsteroidOfToday()

        // basic architecture - goal: display asteroids regardless of internet connection
        // check if database contains asteroid list for today
        getAsteroidsFromRoom()

        // ELSE
        // request download and listen for completion
        // repeat steps above for Room



    }

    private fun getAsteroidsFromRoom() {

        val today = "2022-10-24"

        viewModelScope.launch {

            _status.value = NasaApiStatus.LOADING

            try {

                if (database.get(today) != null){

                    val asteroidCount = database.get(today)!!.size
                    Log.i("-->> Nasa API", "database is ready for today with count $asteroidCount")

                    val neoList = database.get(today)

                    // display asteroids without calling the web API
                    val asteroidList = ArrayList<Asteroid>()
                    if (neoList != null) {
                        for (item in neoList){
                            val asteroidObject = Asteroid(
                                item.neoID,
                                item.codeName,
                                item.closeApproachDate,
                                item.absoluteMagnitude,
                                item.estimatedDiameter,
                                item.relativeVelocity,
                                item.distanceFromEarth,
                                item.isPotentiallyHazardous
                            )
                            asteroidList.add(asteroidObject)
                        }
                        Log.i("-->> Nasa API", "database neo object type conversion OK")
                    }

                    _asteroidListLiveData.value = asteroidList
                    _status.value = NasaApiStatus.DONE

                } else {
                    Log.i("-->> Nasa API", "NULL database")

                    getAsteroidsFromNasa()
                }


            } catch (e: java.lang.Exception) {
                Log.i("-->> Nasa API", "error for getAsteroidsFromNasa " + e.localizedMessage)
            }
        }


    }

    private fun downloadAsteroidOfToday(){
        viewModelScope.launch {
            try {
                _imageToday.value = NasaApi.retrofitServiceForTodaysImage.getImageOfToday()
            } catch (e: java.lang.Exception) {
                Log.i("-->> Nasa API", "error for today's image" + e.localizedMessage)
            }
        }
    }

    private fun getAsteroidsFromNasa() {

        val firstDate = getNextSevenDaysFormattedDates().get(0)
        val lastDate = getNextSevenDaysFormattedDates().get(Constants.DEFAULT_END_DATE_DAYS)
//        Log.i("-->> Nasa API", "First Date $firstDate")
//        Log.i("-->> Nasa API", "Last Date $lastDate")

        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING

            try {

                val jsonString = NasaApi.retrofitServiceForNeos.getAsteroids(firstDate, lastDate, OfflineConstant.API_KEY)
                val nasaResponseObject = Json.decodeFromString<JsonObject>(jsonString)

//                val testItem = nasaResponseObject.jsonObject.get("near_earth_objects") // THIS WORKS!
//                Log.i("-->> Nasa API", "getAsteroidsFromNasa " + testItem) // THIS WORKS!

                val asteroidList : List<Asteroid> = parseAsteroidsJsonResult(nasaResponseObject)

                Log.i("-->> Nasa API", "asteroid list size " + asteroidList.size)

                // display asteroids as soon as possible
                _asteroidListLiveData.value = asteroidList
                _status.value = NasaApiStatus.DONE

                // save asteroids right after displaying it to users
                for (item in asteroidList){

                    val neoItem = NearEarthObject(
                        item.id,
                        item.codename,
                        item.closeApproachDate,
                        item.absoluteMagnitude,
                        item.estimatedDiameter,
                        item.relativeVelocity,
                        item.distanceFromEarth,
                        item.isPotentiallyHazardous,
                    )

                    saveNewAsteroid(neoItem)
                }

            } catch (e: java.lang.Exception) {
                _status.value = NasaApiStatus.ERROR

                Log.i("-->> Nasa API", "error " + e.message)
            }
        }
    }

    fun displayAsteroidInfo(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid

    }

    fun displayAsteroidInfoComplete() {

        val asteroidNull = Asteroid(
            1L,
            "codename",
            "formattedDate",
            1.0,
            10.0,
            10.0,
            1000.0,
            false)

        _navigateToSelectedAsteroid.value = asteroidNull
    }

    private suspend fun insertOneAsteroid(nearEarthObject: NearEarthObject) {
        withContext(Dispatchers.IO) {
            database.insertOneAsteroid(nearEarthObject)
        }
    }

    private fun saveNewAsteroid(nearEarthObject: NearEarthObject) {
        viewModelScope.launch {
            insertOneAsteroid(nearEarthObject)
        }
    }

}