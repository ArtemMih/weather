package com.example.weather.weather_model

import com.google.gson.annotations.SerializedName

data class Snow(@SerializedName("1h") val oneHour: Double = 0.0)
