package com.example.weather.weather_model

data class Hourly(val temp: Double = 0.0,
                  val visibility: Int = 0,
                  val uvi: Int = 0,
                  val pressure: Int = 0,
                  val clouds: Int = 0,
                  val feelsLike: Double = 0.0,
                  val windGust: Double? = 0.0,
                  val dt: Int = 0,
                  val pop: Double = 0.0,
                  val windDeg: Int = 0,
                  val dewPoint: Double = 0.0,
                  val weather: List<WeatherItem>?,
                  val humidity: Int = 0,
                  val windSpeed: Double = 0.0,
                  val rain: Rain?,
                  val snow: Snow?)