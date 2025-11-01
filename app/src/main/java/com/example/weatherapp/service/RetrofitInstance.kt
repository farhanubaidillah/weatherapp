package com.example.weatherapp.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.openweathermap.org/"

    // 🧾 1. Logging Interceptor (untuk melihat request & response di Logcat)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        // BODY -> tampilkan semua detail (URL, headers, body, response)
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ⚙️ 2. OkHttpClient (tambahkan interceptor ke dalam client)
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // 🌐 3. Retrofit Instance (pakai OkHttpClient dan GsonConverter)
    val api: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // pakai client yang sudah ada logging
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}
