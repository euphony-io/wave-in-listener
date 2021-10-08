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
import com.github.doyaaaaaken.kotlincsv.client.CsvWriter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

class VisitorListViewModel(application: Application) : AndroidViewModel(application) {

    var listenData = MutableLiveData<ListenData>()

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private val fileName: String = "VisitorList.csv"

    private val repository = ListenRepository(application)

    // export room to CSV
    fun exportDataToCSV() : Boolean {
        val path: String =
            "${Environment.getExternalStorageDirectory().absolutePath}/Documents/check/"
        val exportDir = File(path)
        var isExportingSuccess = true
        if (!exportDir.exists()) exportDir.mkdirs()

        Thread(Runnable {
            val csvFile = File(exportDir, fileName)
            if (csvFile.exists()) csvFile.delete()
            csvFile.createNewFile()

            val fileWriter = csvWriter {
                charset = "UTF-8"
                delimiter = ','
                nullCode = "NULL"
                lineTerminator = "\n"
            }

            try {
                fileWriter.open(csvFile, append = false) {
                    val visitorList = listenDatabase?.listenDao()?.getAllList()
                    if (visitorList != null) {
                        writeRow(listOf("핸드폰번호", "방문 시각"))
                        for (item in visitorList) {
                            writeRow(listOf(item.personalNumber, item.time))
                        }
                    }
                }
                isExportingSuccess = true;
            } catch (e : Exception) {
                isExportingSuccess = false;
            }
        }).start()

        return isExportingSuccess
    }

    fun getAllVisitorList(): LiveData<List<ListenRoomData>>?{
        return repository.getAll()
    }
}