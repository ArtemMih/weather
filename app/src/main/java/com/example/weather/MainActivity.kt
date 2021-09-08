package com.example.weather

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MainRepository(retrofitService))
        ).get(MainViewModel::class.java)

        viewModel.currentWeather.observe(this, Observer {
            currentTemperature.text = "Темпуратура в Москве ${it.current.temp.toString()} градусов по цельсию"
        })
    }
}