package com.example.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.weather_model.Weather
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))


    private lateinit var locationManager: LocationManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel

    private val retrofitService = RetrofitService.getInstance()
    val hourlyAdapter = HourlyRecyclerViewAdapter()
    val dailyAdapter = DailyRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(binding.root)
        val preference = getPreferences(Context.MODE_PRIVATE)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(MainRepository(retrofitService))
        ).get(MainViewModel::class.java)

//        val myDialogFragment = DIalogGPSOrCountry()
//        val manager = supportFragmentManager
//        myDialogFragment.show(manager, "myDialog")


        binding.hourlyRecyclerView.adapter = hourlyAdapter
        binding.dailyRecyclerView.adapter = dailyAdapter

        viewModel.userLocation.observe(this, Observer {
            viewModel.getWeatherOneCall(it.lat,it.lon)
             city.text = Geocoder(
                 this,
                 Locale("ru")
             ).getFromLocation(it.lat,it.lon,1)[0].locality
        })

        viewModel.currentWeather.observe(this, Observer {
            currentTemperature.text = if (it.current.temp >= 0.0 ) {
                "+${String.format("%.1f",it.current.temp)}°"}
            else{
                "${String.format("%.1f",it.current.temp)}°"}
            feelsLike.text = if (it.current.feelsLike >= 0.0){
                "Ощущается как +${String.format("%.1f",it.current.feelsLike)}°"}
            else{
                "Ощущается как ${String.format("%.1f",it.current.feelsLike)}°"}
            Glide.with(this)
                .load("http://openweathermap.org/img/wn/${it.current.weather?.get(0)?.icon}@4x.png")
                .into(icon)
            description.text = it.current.weather?.get(0)?.description.toString().replaceFirstChar { chr -> chr.uppercaseChar() }
            it.hourly?.let { it1 -> hourlyAdapter.bindWeatherHourly(it1) }
            it.daily?.let { it1 -> dailyAdapter.bindWeatherDaily(it1) }
            val prefsEditor = preference.edit()
            val gson = Gson()
            val json = gson.toJson(it)
            prefsEditor.putString("weather", json)
            prefsEditor.apply()
        })

        viewModel.fail.observe(this, Observer {
            val gson = Gson()
            val json = preference.getString("weather", "")
            val weather: Weather = gson.fromJson(json, Weather::class.java)
            viewModel.setWeather(weather)
            val manager: FragmentManager = supportFragmentManager
            val myDialogFragment = DialogNoInternetConnection()
            myDialogFragment.show(manager, "myDialog")
//            Toast.makeText(this, "Нет соединения с интернетом",
//                Toast.LENGTH_LONG).show()
        })

        viewModel.setLocation(preference.getDouble("lat",56.1167663),preference.getDouble("lon",47.262782))

        gpsIco.setOnClickListener{
            checkGPSPermission()
        }

        refresh.setOnClickListener {
            viewModel.setLocation(preference.getDouble("lat",56.1167663),preference.getDouble("lon",47.262782))
        }
    }


    fun checkPermission(): Boolean{
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun checkGPSPermission(){
        if (checkPermission()){
            getGPSOn()
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER,CancellationTokenSource().token)
                .addOnSuccessListener { location: Location? ->
                    location?.latitude?.let {
                        viewModel.setLocation(it, location.longitude)
                        val preference = getPreferences(Context.MODE_PRIVATE)
                        preference.edit {
                            putDouble("lat", it)
                            putDouble("lon", location.longitude)
                        }
                    }
                }
        }
        else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                101
            )

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getGPSOn()
                    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_LOW_POWER,CancellationTokenSource().token).addOnSuccessListener { location : Location? ->
                        Log.e("Loc",location.toString())
                        location?.latitude?.let { viewModel.setLocation(it,location.longitude) }
                        // Got last known location. In some rare situations this can be null.
                    }


                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun getGPSOn(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (hasGPS){
            Log.e("Loc","GPS alreaady enabled")
        }
        else{
            this.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

}