package com.example.testweather.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testweather.model.MainData
import com.example.testweather.model.MainRepository
import com.example.testweather.model.room.Location
import com.example.testweather.model.room.LocationDao
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val locationDao: LocationDao
) : ViewModel() {

    val newData = MutableLiveData(true)
    val errorMessage = MutableLiveData<String>()
    val errorMessageForDisplay = MutableLiveData<String>()
    val mainData = MutableLiveData<MainData>()
    var job: Job? = null
    val loading = MutableLiveData<Boolean>()
    val showdialog = MutableLiveData(0)
    val cityName = MutableLiveData<String>("NoRealCity")

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        errorMessageForDisplay.postValue(throwable.localizedMessage)
        //Log.v("AlicesEXXXX", "Exception handled: ${throwable.localizedMessage}")
    }

    fun getData(name: String) {
        newData.value = false
        //cityName.value = name
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = mainRepository.getMainCardData(name)
            Log.v("fgdsgfd", response.toString())
            //Log.v("fgsrhtjm", locationDao.getAllLocations().toString())

            if (response.isSuccessful) {
                cityName.postValue(name)
                errorMessageForDisplay.postValue("")
                mainData.postValue(response.body())
                loading.postValue(false)
            } else {
                if (cityName.value == "NoRealCity") {
                    cityName.postValue(name)
                }
                onError("Error : ${response.message()} ")
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    fun insertCityFromInput(id: Int, city: String) {
        viewModelScope.launch {
            locationDao.insertLocation(Location(id, city))
        }
    }

    fun updateLocation(id: Int, city: String) {
        CoroutineScope(Dispatchers.IO).launch {
            locationDao.update(id, city)
        }
    }


}