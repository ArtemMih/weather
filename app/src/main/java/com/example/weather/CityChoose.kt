package com.example.weather

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.weather.databinding.CityChooseBinding
import kotlinx.android.synthetic.main.city_choose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CityChoose: DialogFragment(){

    interface CityChooseListener{
        fun cityChosen(city: String)
    }

    private var _binding: CityChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CityChooseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        find_city.setOnClickListener {
            if (city_name.text.toString() != ""){
                GlobalScope.launch(Dispatchers.Main) {
                    var geo = listOf<String>()
                    withContext(Dispatchers.IO) {
                        try {
                            geo = Geocoder(
                                requireContext(),
                                Locale("ru")
                            ).getFromLocationName(city_name.text.toString(), 5).map { it.featureName }
                        }
                        catch (e: Exception){
                            Log.e("ex",e.toString())
                        }
                    }
                    if (geo.isEmpty()) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item, listOf<String>())
                        city_spinner.adapter = adapter
                    }
                    else{
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.support_simple_spinner_dropdown_item, geo)
                        city_spinner.adapter = adapter
                    }
                }
            }
            else    {
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.support_simple_spinner_dropdown_item, listOf<String>())
                city_spinner.adapter = adapter
            }
        }
        choose_city.setOnClickListener {
            val city_check = city_spinner.selectedItem?.toString() ?: ""
            val listener:CityChooseListener = activity as CityChooseListener
            listener.cityChosen(city_check)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
