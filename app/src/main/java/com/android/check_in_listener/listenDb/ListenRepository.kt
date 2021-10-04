package com.android.check_in_listener.listenDb

import android.app.Application
import androidx.lifecycle.LiveData

class ListenRepository(application: Application) {
    private val listenDao: ListenDao?
    private val visitorList: LiveData<List<ListenRoomData>>?

    init {
        val db = ListenDatabase.getInstance(application)
        listenDao = db?.listenDao()
        visitorList = listenDao?.getAll()
    }

    fun getAll(): LiveData<List<ListenRoomData>>? {
        return listenDao?.getAll()
    }
}