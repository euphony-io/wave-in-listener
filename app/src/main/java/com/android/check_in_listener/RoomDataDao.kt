package com.android.check_in_listener

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

interface RoomDataDao {
    @Query("select * from room_data")
    fun getAll() : List<RoomData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: RoomData)

    @Delete
    fun delete(data: RoomData)
}