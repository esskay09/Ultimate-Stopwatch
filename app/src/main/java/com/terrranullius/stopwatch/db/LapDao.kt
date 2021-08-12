package com.terrranullius.stopwatch.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LapDao : LapTasks {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertLap(lap: Lap)

    @Query("DELETE FROM laps_table")
    override suspend fun deleteAll()

    @Query("SELECT * FROM laps_table")
    override fun getAllLaps(): LiveData<List<Lap>>
}