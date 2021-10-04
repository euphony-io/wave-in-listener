package com.android.check_in_listener.listenDb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListenDao {
    @Query("select * from listenroomdata")
    fun getAll() : LiveData<List<ListenRoomData>>

    @Query("select * from listenroomdata")
    fun getAllList() : List<ListenRoomData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ListenRoomData)

    @Delete
    fun delete(data: ListenRoomData)
}