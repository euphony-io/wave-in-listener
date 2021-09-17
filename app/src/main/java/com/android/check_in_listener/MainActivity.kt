package com.android.check_in_listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() , View.OnClickListener{

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(MainViewModel::class.java)

        model.listenData.observe(this, Observer {
            binding.tvNum.text = it.toString()
        })

        binding.btnListen.setOnClickListener(this)
        binding.btnAllList.setOnClickListener(this)

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when(v){
            binding.btnListen ->
                model.listener()
        }

    }

}