package com.android.check_in_listener

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.android.check_in_listener.databinding.ActivityMainBinding
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRoomData
import com.android.check_in_listener.visitorList.VisitorListActivity
import com.github.ybq.android.spinkit.style.FadingCircle

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 17389
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var isListening = false
    private lateinit var model: MainViewModel

    private var listenDatabase: ListenDatabase? = null

    lateinit var loadingCircle: FadingCircle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        model = ViewModelProvider(this).get(MainViewModel::class.java)

        listenDatabase = ListenDatabase.getInstance(this)

//        model.listenData.observe(this, Observer {
//            binding.tvNum.text = it.toString()
//        })

        requestPermissions()

        setLoadingCircle()

        binding.btnListen.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
            ) {
                showDialogToGetMicroPhonePermission()
            } else {
                if (!isListening) startListen()
                else endListen()
                isListening = model.listener(isListening)
            }
        }

        binding.btnVisitorList.setOnClickListener {
            startActivity(Intent(this, VisitorListActivity::class.java))
        }

        insertDummyData()

    }

    private fun setLoadingCircle(){
        loadingCircle = FadingCircle()
        loadingCircle.color = Color.parseColor("#57AEFF")
    }

    private fun startListen(){
        binding.loadingMain.setIndeterminateDrawable(loadingCircle)
        loadingCircle.setVisible(true, true)
        loadingCircle.start()
        binding.btnListen.text = getString(R.string.listen_end)
    }

    private fun endListen(){
        binding.btnListen.text = getString(R.string.listen_start)
        loadingCircle.stop()
        loadingCircle.setVisible(false, false)
    }

    // 뷰 확인용 더미데이터입니다. 개발 마지막에 지워주세요!
    private fun insertDummyData(){
        saveListenData(ListenRoomData("가1234나1234", null, "2021-02-02 12:30:43"))
        saveListenData(ListenRoomData("가1234나12345", null, "2021-02-03 11:32:43"))
        saveListenData(ListenRoomData("가1234나12346", null, "2021-02-04 23:32:54"))
        saveListenData(ListenRoomData("가1234나12347", null, "2021-02-05 34:32:65"))
        saveListenData(ListenRoomData("가1234나12348", null, "2021-02-06 65:34:43"))
        saveListenData(ListenRoomData("가1234나12349", null, "2021-02-07 64:23:32"))
        saveListenData(ListenRoomData("가1234나123400", null, "2021-02-08 33:44:33"))
        saveListenData(ListenRoomData("가1234나12348", null, "2021-02-09 76:87:55"))
        saveListenData(ListenRoomData("가1234나123476", null, "2021-02-010 45:56:56"))
        saveListenData(ListenRoomData("가1234나12347623", null, "2021-02-08 43:43:65"))
        saveListenData(ListenRoomData("가1234나1234761", null, "2021-02-23 34:44:54"))
        saveListenData(ListenRoomData("가1234나123476342", null, "2021-02-695 34:55:66"))
        saveListenData(ListenRoomData("가1234나12347653", null, "2021-02-49 88:44:55"))
        saveListenData(ListenRoomData("가1234나1234765352", null, "2021-02-23 33:43:22"))
        saveListenData(ListenRoomData("가1234나12347643", null, "2021-02-99 23:43:44"))
    }

    private fun saveListenData(listenData: ListenRoomData) {
        Thread(Runnable {
            listenDatabase?.listenDao()?.insert(listenData)
        }).start()
    }

    private fun requestPermissions(): Boolean {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
            && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        val permissions: Array<String> = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        ActivityCompat.requestPermissions(this, permissions, 0)
        return false

    }

    private fun showDialogToGetMicroPhonePermission() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Permission request")
            .setMessage("you need to allow microphone permission to receive data.")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
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
