package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.HourlyRecyclerViewBinding
import com.example.weather.weather_model.Hourly
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat.getTimeInstance

class HourlyRecyclerViewAdapter: RecyclerView.Adapter<HourlyViewHolder>() {

    var weatherHourly = mutableListOf<Hourly>()

    fun bindWeatherHourly(weatherHourly: List<Hourly>) {
        this.weatherHourly = weatherHourly.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = HourlyRecyclerViewBinding.inflate(inflater, parent, false)
        return HourlyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
        val format =  getTimeInstance()
//        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//        val date = java.util.Date(1532358895 * 1000)
        val weatherHour = weatherHourly[position]
        holder.binding.time.text = format.format(weatherHour.dt * 1000)
        holder.binding.hourlyTemperature.text = if (weatherHour.temp >= 0.0){
            "+${weatherHour.temp}°"}
        else{
            "${weatherHour.temp}°"}
        Glide.with(holder.itemView.context)
            .load("http://openweathermap.org/img/wn/${weatherHour.weather?.get(0)?.icon}@2x.png")
            .into(holder.binding.hourlyIco)
//        holder.binding.hourlyIco.setImageResource(R.drawable.d10)
    }

    override fun getItemCount(): Int {
        return weatherHourly.size
    }

}

class HourlyViewHolder(val binding: HourlyRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
}
