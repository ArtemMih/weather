package com.example.weather


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.weather_model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: MainRepository):  ViewModel() {

    val currentWeather = MutableLiveData<Weather>()
    val userLocation = MutableLiveData<UserLocation>()

    fun getWeatherOneCall(lat:Double,lon: Double) {
        val response = repository.getWeatherOneCall(lat,lon)
        response.enqueue(object : Callback<Weather> {
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                currentWeather.postValue(response.body())

            }
            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Log.e("Fail",t.message.toString())
            }
        })
    }

    fun setLocation(lat:Double, lon:Double){
        userLocation.postValue(UserLocation(lat,lon))
    }



    init {
//        getWeatherOneCall()

    }


}