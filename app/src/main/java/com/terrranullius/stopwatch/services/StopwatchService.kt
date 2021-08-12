package com.terrranullius.stopwatch.services

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.terrranullius.stopwatch.other.Constants.ACTION_RESET_LAP_STOPWATCH
import com.terrranullius.stopwatch.other.Constants.ACTION_START_PAUSE_STOPWATCH
import com.terrranullius.stopwatch.other.Constants.FOREGROUND_STOPWATCH_SERVICE_ID
import com.terrranullius.stopwatch.other.Constants.STOPWATCH_UPDATE_INTERVAL
import com.terrranullius.stopwatch.other.StopwatchUtility
import com.terrranullius.stopwatch.other.createChannel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StopwatchService : LifecycleService() {

    private var startTime = 0L
    private var lastTimeStamp = 0L
    private var totalPreviousTime = 0L
    private var timeRunInMillis = 0L

    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)

    private var lastLapTime = 0L

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    lateinit var notificationManager: NotificationManager

    companion object {
        val totalTimeRunInMillis = MutableLiveData<Long>(0L)
        val timeRunInSeconds = MutableLiveData<Long>(0L)
        val isStopwatchRunning = MutableLiveData<Boolean>(false)
        val isFirstStart = MutableLiveData<Boolean>(true)
        val lastLap = MutableLiveData<Long>(0L)
        val isReset = MutableLiveData<Boolean>(false)
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createChannel()

        isReset.value = false

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_START_PAUSE_STOPWATCH -> startPauseStopwatch()
            ACTION_RESET_LAP_STOPWATCH -> lapResetStopwatch()
        }

        setObservers()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun lapResetStopwatch() {

        if (isStopwatchRunning.value!!) {
            addLap()
        } else {
            resetStopWatch()
        }

    }

    private fun addLap() {

        lastLap.value = totalTimeRunInMillis.value?.minus(lastLapTime)

        lastLapTime = totalTimeRunInMillis.value!!
    }


    private fun startPauseStopwatch() {

        if (isFirstStart.value!!) {

            startForeground(
                FOREGROUND_STOPWATCH_SERVICE_ID,
                notificationBuilder.build()
            )

            isFirstStart.value = false
        }

        //Stopwatch Resumed
        if (!(isStopwatchRunning.value!!)) {
            startTime =
                System.currentTimeMillis()

        } else
        //Stopwatch Paused
        {
            totalPreviousTime += timeRunInMillis

        }

        isStopwatchRunning.value = (!(isStopwatchRunning.value!!))


        if (isStopwatchRunning.value!!) {

            coroutineScope.launch {
                while (isStopwatchRunning.value!!) {

                    timeRunInMillis = System.currentTimeMillis() - startTime

                    totalTimeRunInMillis.value = (totalPreviousTime + timeRunInMillis)

                    if (totalTimeRunInMillis.value!! >= lastTimeStamp + 1000L) {

                        timeRunInSeconds.value = timeRunInSeconds.value!! + 1
                        lastTimeStamp += 1000L
                    }
                    delay(STOPWATCH_UPDATE_INTERVAL)
                }

            }
        }

    }

    private fun setObservers() {
        timeRunInSeconds.observe(this, Observer {
            updateNotification(it)

        })

        isStopwatchRunning.observe(this, Observer {

            if (it) notificationBuilder.setContentTitle("Stopwatch Running")
            else notificationBuilder.setContentTitle("Stopwatch Paused")

            notificationManager.notify(
                FOREGROUND_STOPWATCH_SERVICE_ID,
                notificationBuilder.build()
            )

        })
    }

    private fun updateNotification(it: Long) {

        val formattedTime = StopwatchUtility.getFormattedStopwatchNotificationString(it, false)
        Timber.d("time: $formattedTime")

        notificationBuilder.setContentText(formattedTime)
        notificationManager.notify(
            FOREGROUND_STOPWATCH_SERVICE_ID,
            notificationBuilder.build()
        )
    }

    private fun resetStopWatch() {

        isReset.value = true

        isFirstStart.value = true
        isStopwatchRunning.value = false
        lastLap.value = 0
        startTime = 0L
        lastTimeStamp = 0L
        totalPreviousTime = 0L
        timeRunInMillis = 0L
        totalTimeRunInMillis.value = 0L
        timeRunInSeconds.value = 0L

        stopForeground(true)
        stopSelf()
    }

}