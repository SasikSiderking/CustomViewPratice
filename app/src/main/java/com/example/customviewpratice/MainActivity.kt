package com.example.customviewpratice

import androidx.appcompat.app.AppCompatActivity
import com.example.customviewpratice.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    val binding: ActivityMainBinding
        get() = _binding!!
    private var _binding: ActivityMainBinding? = null
}