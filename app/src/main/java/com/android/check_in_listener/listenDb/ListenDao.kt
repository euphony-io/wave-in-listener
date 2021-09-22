package com.android.check_in_listener.listenDb

import androidx.room.*

@Dao
interface ListenDao {
    @Query("select * from listenroomdata")
    fun getAll() : List<ListenRoomData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ListenRoomData)

    @Delete
    fun delete(data: ListenRoomData)
}