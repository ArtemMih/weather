package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.RecyclerViewHourlyBinding
import com.example.weather.weather_model.Hourly
import com.example.weather.weather_model.Weather
import java.text.DateFormat.getTimeInstance

class MainRecyclerViewAdapter: RecyclerView.Adapter<MainViewHolder>() {

    var weatherHourly = mutableListOf<Hourly>()

    fun bindWeatherHourly(weatherHourly: List<Hourly>) {
        this.weatherHourly = weatherHourly.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerViewHourlyBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val format =  getTimeInstance()
//        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//        val date = java.util.Date(1532358895 * 1000)
        val weatherHour = weatherHourly[position]
        holder.binding.time.text = format.format(weatherHour.dt * 1000)
        holder.binding.hourlyTemperature.text = if (weatherHour.temp >= 0.0){
            "+${weatherHour.temp}°"}
        else{
            "${weatherHour.temp}°"}
        holder.binding.hourlyIco.setImageResource(R.drawable.d10)
    }

    override fun getItemCount(): Int {
        return weatherHourly.size
    }

}

class MainViewHolder(val binding: RecyclerViewHourlyBinding) : RecyclerView.ViewHolder(binding.root) {
}
