package com.terrranullius.stopwatch.db

import androidx.lifecycle.LiveData

interface LapTasks {

    suspend fun insertLap(lap: Lap)

    suspend fun deleteAll()

    fun getAllLaps(): LiveData<List<Lap>>
}