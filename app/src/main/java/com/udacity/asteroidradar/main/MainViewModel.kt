package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.ImageOfToday
import com.udacity.asteroidradar.OfflineConstant
import com.udacity.asteroidradar.api.NasaApi
import kotlinx.coroutines.launch

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<NasaApiStatus>()

    private val _asteroidItems = MutableLiveData<List<Asteroid>>()

    private val _imageToday = MutableLiveData<ImageOfToday>()
    val imageOfToday: LiveData<ImageOfToday>
        get() = _imageToday

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        getAsteroidOfToday()
        getAsteroidsFromNasa()
    }

    private fun getAsteroidOfToday(){
        viewModelScope.launch {
            try {
                _imageToday.value = NasaApi.retrofitService.getImageOfToday()
            } catch (e: java.lang.Exception) {
//                Log.i("-->> Nasa API", "error for today's image" + e.localizedMessage)
            }
        }
    }

    private fun getAsteroidsFromNasa() {
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                _asteroidItems.value = NasaApi.retrofitService.getAsteroids("2022-10-01", "2022-10-02", OfflineConstant.API_KEY)
                _status.value = NasaApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _status.value = NasaApiStatus.ERROR
                _asteroidItems.value = ArrayList()
                Log.i("-->> Nasa API", "error for asteroid list " + e.suppressed.toString())
            }
        }
    }

    fun displayAsteroidInfo(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

}