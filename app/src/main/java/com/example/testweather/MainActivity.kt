package com.example.testweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.testweather.composeui.BackgroundImage
import com.example.testweather.model.MainRepository
import com.example.testweather.model.RetrofitService
import com.example.testweather.model.room.LocationDao
import com.example.testweather.model.room.LocationDatabase
import com.example.testweather.ui.theme.TestWeatherTheme
import com.example.testweather.viewmodel.MainViewModel
import com.example.testweather.viewmodel.MyViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var locationDao: LocationDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            LocationDatabase::class.java, "location_database"
        ).build()
        locationDao = db.locationDao()

        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)

        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository, locationDao)
        ).get(MainViewModel::class.java)



        CoroutineScope(Dispatchers.IO).launch {
            listOf(launch {
                val sizeLocation = locationDao.getAllLocations().size
                if (sizeLocation == 0) {
                    runOnUiThread {
                        viewModel.showdialog.value = 1
                    }
                }
            }).joinAll()



            launch {
                if (locationDao.getAllLocations().size > 0) {
                    val cityFromDB = locationDao.getAllLocations()[0].location
                    runOnUiThread {
                        viewModel.getData(cityFromDB)
                       // viewModel.getData(viewModel.cityName.value ?: cityFromDB)
                    }
                }
            }

            runOnUiThread {
                setContent {
                    TestWeatherTheme {
                        BackgroundImage(viewModel)
                    }
                }
            }
        }
    }

}








