package com.example.customviewpratice

import android.os.Bundle
import android.util.Log
import android.view.View.OnClickListener
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
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

        with(binding){
            fieldOfDreams.setLoadImageCallback(loadImageCallback)
            resetButton.setOnClickListener(resetButtonClickListener)
            seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener)
            seekBar.min = SEEK_BAR_MIN
            seekBar.max = SEEK_BAR_MAX
            seekBar.progress = SEEK_BAR_PROGRESS
        }
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

    private val onSeekBarChangeListener = object: OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            if (seekBar != null) {
                binding.fieldOfDreams.scaleRadius(seekBar.progress/50f)
            }
        }

    }
    companion object{
        const val SEEK_BAR_MIN = 0
        const val SEEK_BAR_MAX = 100
        const val SEEK_BAR_PROGRESS = 50
    }
}