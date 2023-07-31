package com.baljeet.youdotoo.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.baljeet.youdotoo.MainActivity
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.data.local.entities.InvitationEntity

class InvitationNotificationService (
    private val context : Context
){

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(invitation : InvitationEntity){

        val mainActivityIntent = Intent(context,MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY,invitation.id)
        }

        val onClickPendingIntent =  PendingIntent.getActivity(context,1,mainActivityIntent,PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.youdotoo_app_icon)
            .setContentTitle("Project Invitation.")
            .setContentText("${invitation.inviteeName} has invited you to a project : ' ${invitation.projectName} '. See more details about project in app.")
            .setStyle(NotificationCompat.BigTextStyle())
            .setContentIntent(onClickPendingIntent)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }

    companion object{
        const val CHANNEL_ID = "ChannelIDForInvitations"
        const val CHANNEL_NAME = "Invitation Request Notifications"
        const val CHANNEL_DESC = "Shows notification when someone send you a project invite."
        const val INVITATION_ID_KEY = "invitation_id_key"
    }
}