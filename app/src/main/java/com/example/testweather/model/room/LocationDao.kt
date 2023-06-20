package com.example.testweather.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(location: Location)

    @Query("SELECT * FROM locations_table")
    fun getAllLocations(): List<Location>

    @Query("UPDATE locations_table SET location=:newLocation WHERE id = :id")
    fun update(id: Int, newLocation: String)
}