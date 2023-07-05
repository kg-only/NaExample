package com.example.naexample.utils

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.toast(@StringRes text: Int) =
    Toast.makeText(this, getString(text), Toast.LENGTH_SHORT).show()