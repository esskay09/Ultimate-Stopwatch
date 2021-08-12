package com.terrranullius.stopwatch.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laps_table")
data class Lap(
    val lapTime: Long = 0L,
    val timeStamp: Long = 0L,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
) {

}