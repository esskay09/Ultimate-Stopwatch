package com.terrranullius.stopwatch.other

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.os.Build
import com.terrranullius.stopwatch.other.Constants.NOTIFICATION_CHANNEL_ID
import com.terrranullius.stopwatch.other.Constants.NOTIFICATION_CHANNEL_NAME

fun NotificationManager.createChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_LOW
            )
        )
    }
}