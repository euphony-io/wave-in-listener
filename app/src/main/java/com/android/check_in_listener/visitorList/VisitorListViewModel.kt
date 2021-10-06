package com.android.check_in_listener.visitorList

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.check_in_listener.listenDb.ListenDatabase
import com.android.check_in_listener.listenDb.ListenRepository
import com.android.check_in_listener.listenDb.ListenRoomData
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File

class VisitorListViewModel(application: Application) : AndroidViewModel(application) {

    private var listenDatabase: ListenDatabase? = ListenDatabase.getInstance(application)
    private val fileName: String = "VisitorList.csv"

    private val repository = ListenRepository(application)

    // export room to CSV
    fun exportDataToCSV() {
        val path: String =
            "${Environment.getExternalStorageDirectory().absolutePath}/Documents/check/"
        val exportDir = File(path)
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

            fileWriter.open(csvFile, append = false) {
                val visitorList = listenDatabase?.listenDao()?.getAllList()
                if (visitorList != null) {
                    writeRow(listOf("핸드폰번호", "방문 시각"))
                    for (item in visitorList) {
                        writeRow(listOf(item.personalNumber, item.time))
                    }

                }
            }
        }).start()
    }

    fun getAllVisitorList(): LiveData<List<ListenRoomData>>? {
        return repository.getAll()
    }
}