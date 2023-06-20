package com.example.testweather.model

class MainRepository (private val retrofitService: RetrofitService) {
    suspend fun getMainCardData(q:String) = retrofitService.getMainCardData(q = q)
}