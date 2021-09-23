package com.android.check_in_listener

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import euphony.lib.receiver.AcousticSensor
import euphony.lib.receiver.EuRxManager

class MainViewModel : ViewModel() {
    var listenData = MutableLiveData<ListenData>()

    private val mRxManager: EuRxManager by lazy {
        EuRxManager()
    }

    public val requiredPermission = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    // 마이크 권한 설정 필요
    public fun listener(isListening: Boolean): Boolean {
        if(isListening){
//            mRxManager.finish()
            return false
        }else{
//            mRxManager.listen()
            getListenData()
            return true
        }
    }

    private fun getListenData() {
        mRxManager.acousticSensor = AcousticSensor { listenData ->
            var address = ""
            var number = ""
            listenData.split("/").forEachIndexed() { idx, split ->
                when (idx) {
                    0 -> address = split
                    1 -> number = split
                }
            }
            val listenData = ListenData(address, number)

            // 받아온 데이터 저장 필요
        }
    }
}