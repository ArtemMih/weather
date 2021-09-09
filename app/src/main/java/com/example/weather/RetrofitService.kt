package com.example.weather

import com.example.weather.weather_model.Weather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("data/2.5/onecall?exclude=minutely,alerts&lang=ru&units=metric&appid=0e4aca8f09a5e5fd72b5f642de11f837")
    fun getWeatherOneCall(@Query("lat") lat: Double, @Query("lon") lon: Double): Call<Weather>

    companion object {
        var retrofitService: RetrofitService? = null

        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}