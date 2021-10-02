package com.android.check_in_listener.visitorList

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.check_in_listener.ListenData
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRepository
import com.android.check_in_listener.listenDb.ListenRoomData
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class VisitorListViewModel(application: Application) : AndroidViewModel(application) {

    var listenData = MutableLiveData<ListenData>()

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private val fileName: String = "VisitorList.csv"

    private val repository = ListenRepository(application)

    // export room to CSV
    fun exportDataToCSV() {
        val path: String =
            "${Environment.getExternalStorageDirectory().absolutePath}/Documents/VisitorList/"
        val exportDir = File(path)
        if (!exportDir.exists()) exportDir.mkdirs()

        Thread(Runnable {
            val csvFile = File(exportDir, fileName)
            if (csvFile.exists()) csvFile.delete()
            csvFile.createNewFile()
            val fileWriter = PrintWriter(FileWriter(csvFile))

            val visitorList = listenDatabase?.listenDao()?.getAllList()
            if (visitorList != null) {
                for (item in visitorList) {
                    fileWriter.println("${item.personalNumber},${item.address}")
                }
            }
            fileWriter.close()
        }).start()
    }

    fun getAllVisitorList(): LiveData<List<ListenRoomData>>?{
        return repository.getAll()
    }
}