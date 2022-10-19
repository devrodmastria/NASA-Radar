package com.udacity.asteroidradar.main

import android.support.v4.os.IResultReceiver._Parcel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.ImageOfToday
import com.udacity.asteroidradar.api.NasaApi
import kotlinx.coroutines.launch

enum class NasaApiStatus { LOADING, ERROR, DONE }

class MainViewModel : ViewModel() {

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()

    private val _status = MutableLiveData<NasaApiStatus>()

    private val _asteroidItems = MutableLiveData<List<Asteroid>>()

    private val _imageToday = MutableLiveData<ImageOfToday>()

    val navigateToSelectedItem: LiveData<Asteroid>
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
                Log.i("-->> Nasa API", "Response error for today's image")
            }

        }
    }

    private fun getAsteroidsFromNasa() {
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                _asteroidItems.value = NasaApi.retrofitService.getAsteroids()
                _status.value = NasaApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _status.value = NasaApiStatus.ERROR
                _asteroidItems.value = ArrayList()
            }
        }
    }

    fun displayAsteroidInfo(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

}