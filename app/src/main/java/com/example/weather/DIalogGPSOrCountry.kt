package com.example.weather

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DIalogGPSOrCountry : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = MaterialAlertDialogBuilder(it)
                builder.setTitle("Выбор города")
                    .setMessage("Выбрать город вручную или включить GPS?")
                    .setCancelable(false)
                    .setPositiveButton("Включить GPS") { dialog, id ->
                        (activity as MainActivity?)?.checkGPSPermission()
                    }
                    .setNegativeButton("Выбрать вручную")
                        { dialog, id ->
                        }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }