package com.android.check_in_listener

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityMainBinding
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRoomData

class MainActivity : AppCompatActivity(){

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var model: MainViewModel

    private var listenDatabase: ListenDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(MainViewModel::class.java)

        listenDatabase = ListenDatabase.getInstance(this)

        model.listenData.observe(this, Observer {
            binding.tvNum.text = it.toString()
        })

        binding.btnListen.setOnClickListener { model.listener() }

    }

    private fun saveListenData(listenData: ListenRoomData){
        Thread(Runnable {
            listenDatabase?.listenDao()?.insert(listenData)
        }).start()
    }

    override fun onDestroy() {
        _binding = null

        ListenDatabase.destroyInstance()
        listenDatabase = null

        super.onDestroy()
    }
}
