package com.example.testweather.composeui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.testweather.model.Forecastday
import com.example.testweather.ui.theme.Blue
import com.example.testweather.ui.theme.BlueLight
import com.example.testweather.ui.theme.WhiteLight
import com.example.testweather.viewmodel.MainViewModel
import kotlin.math.roundToInt

@Composable
fun WeatherByDay(viewModel: MainViewModel) {

    val weatherByDayList = remember {
        mutableStateOf(listOf<Forecastday>())
    }

    viewModel.mainData.observe(LocalLifecycleOwner.current) {
        weatherByDayList.value = it.forecast?.forecastday ?: listOf()
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp, end = 6.dp, bottom = 6.dp)
        ) {

            weatherByDayList.value.forEach {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                    ),
                    shape = RoundedCornerShape(10.dp),

                    ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WhiteLight),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        it.date?.let { it1 ->
                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp, top = 6.dp, bottom = 6.dp),
                                text = it1,
                                fontSize = 18.sp,
                                color = Blue
                            )
                        }
                        Text(
                            modifier = Modifier
                                .padding(top = 6.dp, bottom = 6.dp),
                            text = it.day?.avgtempC?.roundToInt().toString() + '°',
                            fontSize = 24.sp,
                            color = Blue
                        )
                        AsyncImage(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(top = 6.dp, bottom = 6.dp, end = 6.dp),
                            model = "https:" + it.day?.condition?.icon,
                            contentDescription = "weather_img"
                        )
                    }
                }
            }
        }
    }
}