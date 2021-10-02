package com.android.check_in_listener


import android.app.Application
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.check_in_listener.listenDb.ListenDatabase
import euphony.lib.receiver.AcousticSensor
import euphony.lib.receiver.EuRxManager
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var listenData = MutableLiveData<ListenData>()

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)

    private val mRxManager: EuRxManager by lazy {
        EuRxManager()
    }

     val requiredPermission = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    // 마이크 권한 설정 필요
     fun listener(isListening: Boolean): Boolean {
        if (isListening) {
//            mRxManager.finish()
            return false
        } else {
//            mRxManager.listen()
            getListenData()
            return true
        }
    }

    private fun getListenData() {
        mRxManager.acousticSensor = AcousticSensor { listenData ->
            var address = ""
            var number = ""
            var time = ""
            listenData.split("/").forEachIndexed() { idx, split ->
                when (idx) {
                    0 -> address = split
                    1 -> number = split
                }
            }
            time = getTime()
            val listenData = ListenData(address, number, time)

            // 받아온 데이터 저장 필요
        }
    }

    private fun getTime(): String {
        val longNow = System.currentTimeMillis()
        val tDate = Date(longNow)
        val tDateFormat = SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale("ko", "KR"))
        return tDateFormat.format(tDate)
    }

}