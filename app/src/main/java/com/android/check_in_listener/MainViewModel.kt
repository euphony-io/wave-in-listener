package com.android.check_in_listener

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRoomData
import euphony.lib.receiver.AcousticSensor
import euphony.lib.receiver.EuRxManager
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.PrintWriter

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var listenData = MutableLiveData<ListenData>()

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private val fileName: String = "VisitorList.csv"

    private val mRxManager: EuRxManager by lazy {
        EuRxManager()
    }

    public val requiredPermission = arrayOf(
        android.Manifest.permission.RECORD_AUDIO
    )

    // 마이크 권한 설정 필요
    public fun listener(isListening: Boolean): Boolean {
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

    // export room to CSV
    public fun exportDataToCSV() {
        val path: String =
            "${Environment.getExternalStorageDirectory().absolutePath}/Documents/VisitorList/"
        val exportDir = File(path)
        if (!exportDir.exists()) exportDir.mkdirs()

        Thread(Runnable {
            val csvFile = File(exportDir, fileName)
            if (csvFile.exists()) csvFile.delete()
            csvFile.createNewFile()
            val fileWriter = PrintWriter(FileWriter(csvFile))

            var visitorList = listenDatabase?.listenDao()?.getAll()
            if (visitorList != null) {
                for (item in visitorList) {
                    fileWriter.println("${item.personalNumber},${item.address}")
                }
            }
            fileWriter.close()
        }).start()

    }

}