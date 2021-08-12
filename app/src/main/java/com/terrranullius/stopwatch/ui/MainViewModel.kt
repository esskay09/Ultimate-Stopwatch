package com.terrranullius.stopwatch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.terrranullius.stopwatch.db.Lap
import com.terrranullius.stopwatch.db.LapDao
import com.terrranullius.stopwatch.db.LapTasks
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val lapDao: LapDao) : ViewModel() {

    fun insertLap(lap: Lap) = viewModelScope.launch { lapDao.insertLap(lap) }

    fun deleteAll() = viewModelScope.launch { lapDao.deleteAll() }

    fun getAllLaps() =  lapDao.getAllLaps()

}