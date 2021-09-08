package com.example.weather

class MainRepository(private val retrofitService: RetrofitService) {
    fun getWeatherOneCall() = retrofitService.getWeatherOneCall()
}