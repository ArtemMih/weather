package com.example.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.weather_model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository):  ViewModel() {

    val currentWeather = MutableLiveData<Weather>()

    fun getWeatherOneCall() {
        val response = repository.getWeatherOneCall()
        response.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                currentWeather.postValue(response.body())

            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
            }
        })
    }



    init {
        getWeatherOneCall()

    }


}