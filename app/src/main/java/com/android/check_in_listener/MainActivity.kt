package com.android.check_in_listener

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityMainBinding
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRoomData

class MainActivity : AppCompatActivity(){

    companion object {
        const val PERMISSION_REQUEST_CODE = 17389
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var isListening = false
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

        binding.btnListen.setOnClickListener {

            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED){
                showDialogToGetPermission()
            }else{
                if(!isListening) binding.btnListen.text = "수신중단"
                else binding.btnListen.text = "수신버튼"
                isListening = model.listener(isListening)
            }
        }

        requestRecorderPermission()

    }

    private fun saveListenData(listenData: ListenRoomData){
        Thread(Runnable {
            listenDatabase?.listenDao()?.insert(listenData)
        }).start()
    }

    private fun requestRecorderPermission() {
        if(checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED){
        }else{
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_CODE)
        }
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission request")
            .setMessage("you need to allow recorder permission for receive data.")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("Later") { dialogInterface, i ->

        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        _binding = null

        ListenDatabase.destroyInstance()
        listenDatabase = null

        super.onDestroy()
    }
}
