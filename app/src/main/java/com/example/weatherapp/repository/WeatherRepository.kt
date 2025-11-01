package com.example.weatherapp.repository

import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.service.RetrofitInstance

class WeatherRepository {

    private val api = RetrofitInstance.api

    suspend fun getWeather(city: String): WeatherResponse? {
        return try {
            // 🔍 Bersihkan input dan pastikan tidak kosong
            val cleanCity = city.trim()
            if (cleanCity.isBlank()) {
                Log.e("WeatherRepository", "City name is empty, skipping request.")
                return null
            }

            // ✅ Log city dan API key untuk debug
            Log.d("WeatherRepository", "Requesting weather for city='$cleanCity'")
            Log.d("WeatherRepository", "Using API key='${BuildConfig.WEATHER_API_KEY}'")

            // 🌐 Panggilan API ke OpenWeatherMap
            val response = api.getWeatherByCity(cleanCity, BuildConfig.WEATHER_API_KEY)

            // ✅ Log hasil sukses
            Log.d(
                "WeatherRepository",
                "Response from API: ${response.name}, ${response.main.temp}°C, ${response.weather.firstOrNull()?.main}"
            )

            response
        } catch (e: Exception) {
            // ❌ Log jika terjadi error
            Log.e("WeatherRepository", "Error fetching weather: ${e.message}", e)
            null
        }
    }
}
