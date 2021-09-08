package com.example.weather.weather_model

data class Weather(val current: Current,
                   val timezone: String = "",
                   val timezoneOffset: Int = 0,
                   val daily: List<Daily>?,
                   val lon: Double = 0.0,
                   val hourly: List<Hourly>?,
                   val lat: Double = 0.0)