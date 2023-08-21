package com.baljeet.youdotoo.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.baljeet.youdotoo.R

open class DoTooNotification  {



    fun showNotification(
        title: String,
        contentText: String,
        pendingIntent: PendingIntent,
        requestCode : Int,
        context : Context
    ) {

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context,
            NotificationHelper.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.youdotoo_app_icon)
            .setContentTitle(title)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(
            requestCode,
            notification
        )
    }
}