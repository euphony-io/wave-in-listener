package com.android.check_in_listener

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "room_data")
class RoomData {
    @ColumnInfo
    var address:String = ""
    @ColumnInfo
    var num:String = ""

    constructor(address:String, num:String) {
        this.address = address
        this.num = num
    }
}