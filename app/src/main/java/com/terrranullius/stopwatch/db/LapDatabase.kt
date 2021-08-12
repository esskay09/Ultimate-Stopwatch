package com.terrranullius.stopwatch.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = arrayOf(Lap::class),
    version = 1
)
abstract class LapDatabase : RoomDatabase() {
    abstract fun getLapDao(): LapDao
}