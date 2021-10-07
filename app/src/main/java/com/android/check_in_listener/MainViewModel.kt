package com.android.check_in_listener


import android.app.Application
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRoomData
import euphony.lib.receiver.AcousticSensor
import euphony.lib.receiver.EuRxManager
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private var isListening = false

    private val _isSuccess = MutableLiveData<Boolean>(false)
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val mRxManager: EuRxManager by lazy {
        EuRxManager()
    }

    val requiredPermission = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    fun listener(): Boolean {
        _isSuccess.value = false
        if (isListening) {
            mRxManager.finish()
            isListening = false
            return false
        } else {
            mRxManager.listen()
            getListenData()
            isListening = true
            return true
        }
    }

    private fun saveListenData(listenData: ListenRoomData) {
        Thread(Runnable {
            listenDatabase?.listenDao()?.insert(listenData)
        }).start()

        _isSuccess.value = true
    }

    private fun getListenData() {
        mRxManager.acousticSensor = AcousticSensor { listenData ->
            Log.d("listen", "MainViewModel - getListenData() : $listenData")
            val personalNumber = listenData
            val time = getTime()
            saveListenData(ListenRoomData(personalNumber, time))
            isListening = false
        }
    }

    private fun getTime(): String {
        val longNow = System.currentTimeMillis()
        val tDate = Date(longNow)
        val tDateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale("ko", "KR"))
        return tDateFormat.format(tDate)
    }

}