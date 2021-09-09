package com.example.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.DailyRecyclerViewBinding

import com.example.weather.weather_model.Daily

class DailyRecyclerViewAdapter: RecyclerView.Adapter<DailyViewHolder>()  {

    var weatherDaily = mutableListOf<Daily>()

    fun bindWeatherDaily(weatherDaily: List<Daily>) {
        this.weatherDaily = weatherDaily.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DailyRecyclerViewBinding.inflate(inflater, parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
//        val format1 = DateFormat.getDateInstance()
//        val format2 = DateFormat.getDateInstance(DateFormat.DAY_OF_WEEK_FIELD)
        val sdfWeekday = java.text.SimpleDateFormat("EEEE")
        val sdfMonth = java.text.SimpleDateFormat("d MMMM")
        val weatherDay = weatherDaily[position]
        holder.binding.date1.text = sdfWeekday.format(weatherDay.dt * 1000).toString().replaceFirstChar { chr -> chr.uppercaseChar() }
        holder.binding.date2.text = sdfMonth.format(weatherDay.dt * 1000).toString()
        holder.binding.dailyTemperatureDay.text = if (weatherDay.temp.day >= 0.0){
            "+${String.format("%.1f",weatherDay.temp.day)}°"}
        else{
            "${String.format("%.1f",weatherDay.temp.day)}°"}
        holder.binding.dailyTemperatureNight.text = if (weatherDay.temp.night >= 0.0){
            "+${String.format("%.1f",weatherDay.temp.night)}°"}
        else{
            "${String.format("%.1f",weatherDay.temp.night)}°"}
        Glide.with(holder.itemView.context)
            .load("http://openweathermap.org/img/wn/${weatherDay.weather?.get(0)?.icon}@4x.png")
            .into(holder.binding.dayIco)
//        holder.binding.dayIco.setImageResource(R.drawable.d10)
    }

    override fun getItemCount(): Int {
        return weatherDaily.size
    }
}

class DailyViewHolder(val binding: DailyRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
}
