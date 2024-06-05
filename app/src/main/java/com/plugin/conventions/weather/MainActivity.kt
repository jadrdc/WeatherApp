package com.plugin.conventions.weather

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.plugin.conventions.weather.base.BaseActivity
import com.plugin.conventions.weather.ui.theme.WeatherAppTheme
import com.plugin.conventions.weather.viewmodel.weather.WeatherDataViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    WeatherScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun WeatherScreen(viewModel: WeatherDataViewModel = koinViewModel()) {

        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
        LaunchedEffect(permissionsState) {
            permissionsState.permissions.forEach { permissionState ->
                snapshotFlow { permissionState }.collect { newState ->
                    println("Permission ${permissionState.permission} has changed to $newState")
                }
            }
        }
        if (permissionsState.allPermissionsGranted) {
            viewModel.startObserving(true)
        } else {
            viewModel.startObserving(false)
        }


        val state = viewModel.state.value
        state.data?.let {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    ImageFromUrl(state.data?.url ?: "")
                    Text(
                        text = state.data?.location ?: "",
                    )
                    Text(text = (state.data?.temperature + " C") ?: "0 C", fontSize = 42.sp)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row {
                            Text(
                                text = "Min.Temp ", fontSize = 12.sp
                            )
                            Text(
                                text = (state.data?.minTemperature + " C") ?: "0 C",
                                fontSize = 12.sp
                            )
                        }
                        Row {
                            Text(
                                text = "Max.Temp ", fontSize = 12.sp
                            )
                            Text(
                                text = (state.data?.maxTemperature + " C") ?: "0 C",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageFromUrl(url: String) {
    val painter: Painter = rememberAsyncImagePainter(url)

    Image(
        painter = painter, contentDescription = "Image from URL",
        modifier = Modifier
            .size(100.dp), // Adjust padding as needed
    )
}