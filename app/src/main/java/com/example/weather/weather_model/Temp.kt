package com.example.weather.weather_model

data class Temp(val min: Double = 0.0,
                val max: Int = 0,
                val eve: Double = 0.0,
                val night: Double = 0.0,
                val day: Double = 0.0,
                val morn: Double = 0.0)