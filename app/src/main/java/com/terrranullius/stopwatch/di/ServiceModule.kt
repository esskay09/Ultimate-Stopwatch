package com.terrranullius.stopwatch.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.terrranullius.stopwatch.R
import com.terrranullius.stopwatch.other.Constants
import com.terrranullius.stopwatch.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {


    @ServiceScoped
    @Provides
    fun providesBaseNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(
        app, Constants.NOTIFICATION_CHANNEL_ID
    )
        .setContentTitle("Stopwatch Running")
        .setAutoCancel(false)
        .setContentText("00:00:00")
        .setOngoing(true)
        .setSmallIcon(R.mipmap.icc)
        .setContentIntent(pendingIntent)


    @ServiceScoped
    @Provides
    fun providesPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent {
        val intent = Intent(app, MainActivity::class.java)
        return PendingIntent.getActivity(
            app,
            Constants.ACTION_START_STOPWATCH_FRAGMENT_FROM_NOTIFICATION,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}