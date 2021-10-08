package com.android.check_in_listener.visitorList

import android.app.Application
import android.os.Environment
import android.os.HandlerThread
import android.telecom.Call
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.check_in_listener.ListenData
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRepository
import com.android.check_in_listener.listenDb.ListenRoomData
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.lang.Exception
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class VisitorListViewModel(application: Application) : AndroidViewModel(application) {

    var listenData = MutableLiveData<ListenData>()

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private val fileName: String = "VisitorList.csv"
    private val path: String =
        "${Environment.getExternalStorageDirectory().absolutePath}/Documents/VisitorsList/"

    private val exportDir = File(path)
    private val csvFile = File(exportDir, fileName)

    private var isExportingSuccess = false

    private val repository = ListenRepository(application)

    // export room to CSV
    fun exportDataToCSV(): Boolean {
        if (!exportDir.exists()) exportDir.mkdirs()

        val callable = object : Callable<Boolean> {
            override fun call(): Boolean {
                if (csvFile.exists()) csvFile.delete()
                csvFile.createNewFile()

                val fileWriter = csvWriter {
                    charset = "UTF-8"
                    delimiter = ','
                    nullCode = "NULL"
                    lineTerminator = "\n"
                }

                fileWriter.open(csvFile, append = false) {
                    val visitorList = listenDatabase?.listenDao()?.getAllList()
                    if (visitorList != null) {
                        writeRow(listOf("핸드폰번호", "방문 시각"))
                        for (item in visitorList) {
                            writeRow(listOf(item.personalNumber, item.time))
                        }
                    }
                }
                return true
            }
        }

        isExportingSuccess = writeFileFromRoom(callable)

        return isExportingSuccess
    }

    private fun writeFileFromRoom(callable: Callable<Boolean>): Boolean {
        try {
            return callable.call()
            // return true
        } catch (e: Exception) {
            Log.d("checking", "Fail : ${e.message}")
            return false
        }
    }

    fun getAllVisitorList(): LiveData<List<ListenRoomData>>? {
        return repository.getAll()
    }
}
