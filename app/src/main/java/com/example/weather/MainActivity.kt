package com.example.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weather.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val hourlyAdapter = MainRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MainRepository(retrofitService))
        ).get(MainViewModel::class.java)

        binding.recyclerViewHourly.adapter = hourlyAdapter
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
            icon.setImageResource(R.drawable.d10) //= R.drawable.ic_launcher_background
            description.text = it.current.weather?.get(0)?.description ?: "Ясно"
            it.hourly?.let { it1 -> hourlyAdapter.bindWeatherHourly(it1) }
        })
    }
}