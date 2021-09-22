package com.android.check_in_listener.listenDb

import androidx.room.*

@Dao
interface ListenDao {
    @Query("select * from listendata")
    fun getAll() : List<ListenData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: ListenData)

    @Delete
    fun delete(data: ListenData)
}