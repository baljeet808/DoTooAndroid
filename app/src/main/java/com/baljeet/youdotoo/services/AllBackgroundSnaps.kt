package com.baljeet.youdotoo.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.AppNotificationChannelID


class AllBackgroundSnaps : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            ServiceActions.START.toString() -> start()
            ServiceActions.STOP.toString() -> stopSelf()
        }

        return super.onStartCommand(intent, flags, startId)
    }



    enum class ServiceActions{
        START,STOP
    }

    private fun start(){
        val notification = NotificationCompat.Builder(
            this,
            AppNotificationChannelID
        )
            .setSmallIcon(R.drawable.youdotoo_app_icon)
            .setContentTitle("Sync is on.")
            .setContentText("Keeping your tasks synced with server.")
            .build()
        startForeground(1,notification)
    }

}