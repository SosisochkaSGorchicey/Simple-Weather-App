package com.example.testweather.model


import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val MY_KEY = "3571a5fb99a34fcd980132340232602"

interface RetrofitService {


    @GET("forecast.json")
    suspend fun getMainCardData(
        @Query("key") key: String = MY_KEY,
        @Query("q") q: String,
        @Query("days") days: String = "7",
        @Query("aqi") aqi: String = "no",
        @Query("alerts") alerts: String = "no"
    ) : Response<MainData>

    companion object {

        var retrofitService: RetrofitService? = null

        

        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.weatherapi.com/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}