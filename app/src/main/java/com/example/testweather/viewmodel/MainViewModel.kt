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
import kotlinx.coroutines.withContext

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
        //Log.v("ErrorFromCoroutine", "Exception handled: ${throwable.localizedMessage}")
    }

    fun getData(name: String) {
        newData.value = false
        //cityName.value = name
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {

            val response = mainRepository.getMainCardData(name)
            Log.v("ResponseFromApi", response.toString())
            //Log.v("LocationsFromDB", locationDao.getAllLocations().toString())

            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    cityName.value = name
                    errorMessageForDisplay.value = ""
                    mainData.value = response.body()
                    loading.value = false
                }
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