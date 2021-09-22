package com.android.check_in_listener.listenDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ListenData(
    @PrimaryKey val personalNumber: String?,
    @ColumnInfo(name = "address") val address: String?
)