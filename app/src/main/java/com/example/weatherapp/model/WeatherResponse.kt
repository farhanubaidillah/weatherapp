package com.example.weatherapp.model

data class WeatherResponse(
    val name: String,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val sys: Sys,
    val rain: Rain? = null,
    val timezone: Int
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double
)

data class Clouds(
    val all: Int
)

data class Sys(
    val sunrise: Int,
    val sunset: Int
)

data class Rain(
    // OpenWeatherMap kadang mengirim "1h" (jumlah hujan 1 jam terakhir)
    val `1h`: Double? = 0.0
)
