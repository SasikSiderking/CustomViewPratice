package com.example.customviewpratice

import android.os.Bundle
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.customviewpratice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding
        get() = _binding!!
    private var _binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fieldOfDreams.setLoadImageCallback(loadImageCallback)
        binding.resetButton.setOnClickListener(resetButtonClickListener)
    }

    private val loadImageCallback = LoadImageCallback {
        Glide.with(this)
            .load(it)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .centerCrop()
            .into(binding.imageView)
    }

    private val resetButtonClickListener = OnClickListener{
        binding.imageView.setImageResource(0)
        binding.fieldOfDreams.reset()
    }
}