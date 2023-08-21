package com.baljeet.youdotoo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.baljeet.youdotoo.services.NotificationHelper
import com.baljeet.youdotoo.services.SyncNotificationService
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class DotooApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createSyncNotificationChannel()
        createInvitationNotificationChannel()
    }



    private fun createSyncNotificationChannel(){
        val channel = NotificationChannel(
            SyncNotificationService.CHANNEL_ID_FOR_SYNC,
            SyncNotificationService.CHANNEL_NAME_FOR_SYNC,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = SyncNotificationService.CHANNEL_DESC_FOR_SYNC
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createInvitationNotificationChannel(){
        val channel = NotificationChannel(
            NotificationHelper.CHANNEL_ID,
            NotificationHelper.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.description = NotificationHelper.CHANNEL_DESC
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}