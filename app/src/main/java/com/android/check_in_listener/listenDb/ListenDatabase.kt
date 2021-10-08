package com.android.check_in_listener.listenDb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ListenRoomData::class], version = 2, exportSchema = false)
abstract class ListenDatabase: RoomDatabase() {
    abstract fun listenDao(): ListenDao

    companion object {
        private var INSTANCE: ListenDatabase? = null

        fun getInstance(context: Context): ListenDatabase? {
            if (INSTANCE == null) {
                synchronized(ListenDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ListenDatabase::class.java, "listenRoomData.db"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }

}