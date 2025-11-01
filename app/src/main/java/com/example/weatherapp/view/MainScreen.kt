package com.example.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(viewModel: WeatherViewModel = viewModel()) {
    val weatherState by viewModel.weather.collectAsState()
    val errorState by viewModel.error.collectAsState()
    val context = LocalContext.current

    var city by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            // Search bar (tidak ikut scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = city,
                    onValueChange = { city = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    placeholder = { Text("Enter city") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),

                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { if (city.isNotBlank()) viewModel.getWeather(city) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Search")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                weatherState?.let { weather ->
                    val sdf = SimpleDateFormat("MMMM dd", Locale.getDefault())
                    val date = sdf.format(Date())

                    item {
                        Text(
                            text = weather.name,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = date,
                            fontSize = 18.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val pandaRes = when (weather.weather.firstOrNull()?.main) {
                            "Clear" -> R.drawable.panda_clear
                            "Clouds" -> R.drawable.panda_cloud
                            "Rain" -> R.drawable.panda_rain
                            else -> R.drawable.panda_clear
                        }

                        Image(
                            painter = painterResource(id = pandaRes),
                            contentDescription = "Panda",
                            modifier = Modifier.size(150.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${weather.main.temp.toInt()}°C",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = weather.weather.firstOrNull()?.main ?: "",
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Info grid
                    item {
                        WeatherInfoGrid(weather)
                    }
                }

                errorState?.let { errorMsg ->
                    item {
                        Text(
                            text = "Error: $errorMsg",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherInfoGrid(weather: com.example.weatherapp.model.WeatherResponse) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoCard(R.drawable.humidity, "${weather.main.humidity}%", "Humidity")
            InfoCard(R.drawable.wind, "${weather.wind.speed} km/h", "Wind")
            InfoCard(R.drawable.temperature, "${weather.main.feels_like.toInt()}°", "Feels Like")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoCard(R.drawable.rain, "${weather.rain?.`1h` ?: 0.0} mm", "Rain Fall")
            InfoCard(R.drawable.pressure, "${weather.main.pressure} hPa", "Pressure")
            InfoCard(R.drawable.clouds, "${weather.clouds.all}%", "Clouds")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val sunrise = formatTime(weather.sys.sunrise, weather.timezone)
            val sunset = formatTime(weather.sys.sunset, weather.timezone)
            InfoCard(R.drawable.sunrise, sunrise, "Sunrise")
            InfoCard(R.drawable.sunset, sunset, "Sunset")
        }
    }
}

@Composable
fun InfoCard(iconRes: Int, value: String, label: String) {
    Card(
        modifier = Modifier
            .padding(6.dp)
            .width(100.dp)
            .height(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(28.dp)
            )
            Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = label, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

fun formatTime(timestamp: Int, timezone: Int): String {
    val date = Date((timestamp + timezone) * 1000L)
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(date)
}
