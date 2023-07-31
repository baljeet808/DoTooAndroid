package com.baljeet.youdotoo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.baljeet.youdotoo.common.AppNotificationChannelID
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class DotooApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            AppNotificationChannelID,
            "You do too tasks notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}