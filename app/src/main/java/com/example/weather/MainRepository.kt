package com.example.weather

class MainRepository(private val retrofitService: RetrofitService) {
    fun getWeatherOneCall(lat:Double,lon:Double) = retrofitService.getWeatherOneCall(lat, lon)
}