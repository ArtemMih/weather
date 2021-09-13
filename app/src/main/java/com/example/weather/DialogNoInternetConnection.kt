package com.example.weather

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogNoInternetConnection : DialogFragment(){
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = MaterialAlertDialogBuilder(it)
                builder.setTitle("Нет соединения с интернетом")
                    .setMessage("Произошла ошибка при загрузке данных.\nПроверьте подключение к сети.\nОбновите страницу.\nБудут показаны данные с прошлой сессии.")
                    .setCancelable(false)
                    .setPositiveButton("Ок") { dialog, id ->
                        dialog.cancel()
                    }
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }