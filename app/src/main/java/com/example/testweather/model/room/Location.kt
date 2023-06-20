package com.example.testweather.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_table")
data class Location (

    @PrimaryKey
    var id: Int,

    var location: String
)