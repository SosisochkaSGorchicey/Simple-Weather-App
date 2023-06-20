package com.example.testweather.model

import com.google.gson.annotations.SerializedName

data class MainData (
    @SerializedName("location") var location: Location? = Location(),
    @SerializedName("current") var current: Current?  = Current(),
    @SerializedName("forecast") var forecast: Forecast? = Forecast()
)

data class Location (
    @SerializedName("name") var name: String? = null,
    @SerializedName("localtime") var localtime: String? = null
)

data class Current (
    @SerializedName("temp_c") var tempC: Int? = null,
    @SerializedName("condition") var condition: Condition? = Condition(),
)

data class Condition (
    @SerializedName("text") var text: String? = null,
    @SerializedName("icon") var icon: String? = null
)


data class Day (
    @SerializedName("maxtemp_c") var maxtempC: Double?    = null,
    @SerializedName("mintemp_c") var mintempC: Double?    = null,
    @SerializedName("avgtemp_c") var avgtempC: Double?    = null,
    @SerializedName("totalsnow_cm") var totalsnowCm: Int?       = null,
    @SerializedName("condition") var condition: Condition? = Condition()
)

data class Forecast (
    @SerializedName("forecastday") var forecastday: ArrayList<Forecastday> = arrayListOf()
)

data class Forecastday (
    @SerializedName("date") var date  : String?         = null,
    @SerializedName("day") var day: Day? = Day(),
    @SerializedName("hour") var hour  : ArrayList<Hour> = arrayListOf()
)


data class Hour (
    @SerializedName("time") var time: String?    = null,
    @SerializedName("temp_c") var tempC: Double?       = null,
    @SerializedName("condition") var condition: Condition? = Condition()
)





