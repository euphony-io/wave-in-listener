package com.android.check_in_listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.check_in_listener.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        _binding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}