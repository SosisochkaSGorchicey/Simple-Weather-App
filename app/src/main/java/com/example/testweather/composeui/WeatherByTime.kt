package com.example.testweather.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.testweather.model.Hour
import com.example.testweather.ui.theme.Blue
import com.example.testweather.ui.theme.BlueLight
import com.example.testweather.ui.theme.WhiteLight
import com.example.testweather.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherByTime(viewModel: MainViewModel) {

    val weatherByTimeList = remember {
        mutableStateOf(arrayListOf<Hour>())
    }

    val weatherByTimeListHelper = remember {
        mutableStateOf(arrayListOf<Hour>())
    }

    viewModel.mainData.observe(LocalLifecycleOwner.current) {
        weatherByTimeListHelper.value = it.forecast?.forecastday?.get(0)?.hour ?: arrayListOf()
        weatherByTimeList.value = weatherByTimeListHelper.value.filter {
            it.time.toString().substringAfterLast(' ').substringBefore(':')
                .toInt() > (viewModel.mainData.value?.location?.localtime.toString()
                .substringAfterLast(' ').substringBefore(':')
                .toInt())
        } as ArrayList<Hour>

        weatherByTimeListHelper.value = it.forecast?.forecastday?.get(1)?.hour ?: arrayListOf()
        weatherByTimeListHelper.value = weatherByTimeListHelper.value.filter {
            it.time.toString().substringAfterLast(' ').substringBefore(':')
                .toInt() <= (viewModel.mainData.value?.location?.localtime.toString()
                .substringAfterLast(' ').substringBefore(':')
                .toInt())
        } as ArrayList<Hour>

        weatherByTimeList.value += weatherByTimeListHelper.value
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = BlueLight,
        )
    ) {
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(10.dp),

            ) {
            weatherByTimeList.value.forEach {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    ),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(WhiteLight)
                            .padding(5.dp),
                    ) {
                        Text(
                            text = it.time.toString().substringAfterLast(' '),
                            color = Blue
                        )
                        Text(
                            text = it.tempC?.roundToInt().toString() + '°',
                            color = Blue
                        )
                        AsyncImage(
                            modifier = Modifier
                                .size(35.dp),
                            model = "https:" + it.condition?.icon,
                            contentDescription = "weather_img"
                        )
                    }
                }
            }
        }
    }
}
