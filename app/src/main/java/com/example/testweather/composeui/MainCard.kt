package com.example.testweather.composeui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.testweather.R
import com.example.testweather.ui.theme.BlueLight
import com.example.testweather.viewmodel.MainViewModel

@Composable
fun MainCard(viewModel: MainViewModel) {

    val mainCardLocalTime = remember {
        mutableStateOf("")
    }

    val mainCardName = remember {
        mutableStateOf("")
    }

    val mainCardTemp = remember {
        mutableStateOf("")
    }

    val mainCardMinMaxTemp = remember {
        mutableStateOf("")
    }

    val mainCardDesc = remember {
        mutableStateOf("")
    }

    val mainCardIcon = remember {
        mutableStateOf("")
    }

    viewModel.mainData.observe(LocalLifecycleOwner.current) {
        mainCardLocalTime.value = it.location?.localtime.toString()
        mainCardName.value = it.location?.name.toString()
        mainCardTemp.value = it.current?.tempC.toString() + '°'
        mainCardMinMaxTemp.value =
            it.forecast?.forecastday?.get(0)?.day?.mintempC.toString() + '°' +
                    '/' + it.forecast?.forecastday?.get(0)?.day?.maxtempC.toString() + '°'
        mainCardDesc.value = it.current?.condition?.text.toString()
        mainCardIcon.value = it.current?.condition?.icon.toString()
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
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = mainCardLocalTime.value,
                    fontSize = 16.sp,
                    color = Color.White
                )

                val openDialog = rememberSaveable {
                    mutableStateOf(false)
                }

                if (openDialog.value) {
                    DialogSearch(
                        openDialog, onSubmit = {
                            viewModel.getData(it.filterNot{ it.isWhitespace() })
                            viewModel.updateLocation(0, it)
                        }
                    )
                }

                IconButton(onClick = {
                    openDialog.value = true
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }

            Text(
                text = mainCardName.value,
                color = Color.White,
                fontSize = 20.sp
            )

            Text(
                text = mainCardTemp.value,
                color = Color.White,
                fontSize = 40.sp
            )

            Text(
                text = mainCardMinMaxTemp.value,
                color = Color.White,
                fontSize = 16.sp
            )

            Text(
                text = mainCardDesc.value,
                color = Color.White,
                fontSize = 16.sp
            )

            AsyncImage(
                modifier = Modifier
                    .size(50.dp)
                    .padding(bottom = 5.dp),
                model = "https:" + mainCardIcon.value,
                contentDescription = "weather_img"
            )
        }
    }

}


