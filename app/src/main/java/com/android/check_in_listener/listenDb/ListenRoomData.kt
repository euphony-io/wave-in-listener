package com.android.check_in_listener.listenDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ListenRoomData(
    @PrimaryKey val personalNumber: String,
    @ColumnInfo(name = "address") val address: String?
)