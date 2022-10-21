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
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<NasaApiStatus>()

    private val _imageToday = MutableLiveData<ImageOfToday>()
    val imageOfToday: LiveData<ImageOfToday>
        get() = _imageToday

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _asteroidItemsLiveData = MutableLiveData<ArrayList<Asteroid>>()

    init {
        getAsteroidOfToday()
        getAsteroidsFromNasa()
    }

    private fun getAsteroidOfToday(){
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
        Log.i("-->> Nasa API", "First Date $firstDate")
        Log.i("-->> Nasa API", "Last Date $lastDate")

        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {

                val jsonString = NasaApi.retrofitServiceForNeos.getAsteroids(firstDate, lastDate, OfflineConstant.API_KEY)
                val nasaResponseObject = Json.decodeFromString<JsonObject>(jsonString)
                val testItem = nasaResponseObject.jsonObject.get("near_earth_objects") // THIS WORKS!
//                Log.i("-->> Nasa API", "getAsteroidsFromNasa " + testItem)

                val asteroidList : ArrayList<Asteroid> = parseAsteroidsJsonResult(nasaResponseObject)

                _status.value = NasaApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _status.value = NasaApiStatus.ERROR

                Log.i("-->> Nasa API", "error " + e.message)
            }
        }
    }

    fun displayAsteroidInfo(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

}