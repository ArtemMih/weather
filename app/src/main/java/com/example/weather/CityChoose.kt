package com.example.weather

import android.R
import android.app.Dialog
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.weather.databinding.CityChooseBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.city_choose.*
import java.util.*

class CityChoose: DialogFragment(){

    interface CityChooseListener{
        fun citychosen(city: String)
    }

    private var _binding: CityChooseBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        find_city.setOnClickListener {
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item, Geocoder(
                    requireContext(),
                    Locale("ru")
                ).getFromLocationName(city_name.text.toString(),5).map { it.locality })
            city_spinner.adapter = adapter
        }
        choose_city.setOnClickListener {
            val city_check = city_spinner.selectedItem?.toString() ?: ""
            val listener:CityChooseListener = activity as CityChooseListener
            listener.citychosen(city_check)
            dismiss()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
