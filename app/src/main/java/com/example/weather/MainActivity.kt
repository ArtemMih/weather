package com.example.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val hourlyAdapter = HourlyRecyclerViewAdapter()
    val dailyAdapter = DailyRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MainRepository(retrofitService))
        ).get(MainViewModel::class.java)

        binding.hourlyRecyclerView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter
//        binding.recyclerViewHourly.layoutManager = LinearLayoutManager(this)

        viewModel.currentWeather.observe(this, Observer {
            currentTemperature.text = if (it.current.temp >= 0.0 ) {
                "+${it.current.temp}°"}
            else{
                "${it.current.temp}°"}
            feelsLike.text = if (it.current.feelsLike >= 0.0){
                "Ощущается как +${it.current.feelsLike}°"}
            else{
                "Ощущается как ${it.current.feelsLike}°"}
            Glide.with(this)
                .load("http://openweathermap.org/img/wn/${it.current.weather?.get(0)?.icon}.png")
                .into(icon)
//            icon.setImageResource(R.drawable.d10) //= R.drawable.ic_launcher_background
            description.text = it.current.weather?.get(0)?.description ?: "Ясно"
            it.hourly?.let { it1 -> hourlyAdapter.bindWeatherHourly(it1) }
            it.daily?.let { it1 -> dailyAdapter.bindWeatherDaily(it1) }
        })
    }
}