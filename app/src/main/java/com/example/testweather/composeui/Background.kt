package com.example.testweather.composeui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.testweather.R
import com.example.testweather.ui.theme.Blue
import com.example.testweather.ui.theme.ErrorColor
import com.example.testweather.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun BackgroundImage(viewModel: MainViewModel) {

    Image(
        painter = painterResource(id = R.drawable.background_sky),
        contentDescription = "sky",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.8f),
        contentScale = ContentScale.FillBounds
    )

    val openDialog = rememberSaveable {
        if (viewModel.showdialog.value == 1) {
            mutableStateOf(true)
        } else {
            mutableStateOf(false)
        }

    }

    if (openDialog.value) {
        DialogSearch(
            openDialog, onSubmit = {
                viewModel.insertCityFromInput(0, it.filterNot{ it.isWhitespace() })
                viewModel.showdialog.value = 2
                viewModel.getData(it)
            }
        )
    }

    BackgroundLayout(viewModel)

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BackgroundLayout(viewModel: MainViewModel) {

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        viewModel.getData(viewModel.cityName.value ?: "London")
        delay(1500)
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    val internetMessage = remember { mutableStateOf("") }

    viewModel.errorMessageForDisplay.observe(LocalLifecycleOwner.current) {
        if (it.isNotEmpty()) {
            if (it.startsWith("Unable")) {
                internetMessage.value = "No Internet connection"
            } else {
                internetMessage.value = "Wrong input"
                //internetMessage.value = it + viewModel.cityName.value
            }
        } else {
            internetMessage.value = ""
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(state)
    ) {
        PullRefreshIndicator(
            refreshing,
            state,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = Blue
        )


        LazyColumn {
            if (internetMessage.value.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = internetMessage.value,
                            color = ErrorColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                MainCard(viewModel)
            }

            item {
                WeatherByTime(viewModel)
            }

            item {
                WeatherByDay(viewModel)
            }

        }
    }
}