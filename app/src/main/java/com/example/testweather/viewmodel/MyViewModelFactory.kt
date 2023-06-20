package com.example.testweather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testweather.model.MainRepository
import com.example.testweather.model.room.LocationDao

class MyViewModelFactory(private val repository: MainRepository, private val locationDao: LocationDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(this.repository, this.locationDao) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}