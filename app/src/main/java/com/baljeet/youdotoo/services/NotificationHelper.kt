package com.baljeet.youdotoo.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.baljeet.youdotoo.MainActivity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    @ApplicationContext val context: Context,
) : DoTooNotification() {


    fun showInviteNotification(invitation: InvitationEntity, title: String, contentText: String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY, invitation.id)
        }
        val onClickPendingIntent =
            PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        showNotification(
            title = title,
            contentText = contentText,
            pendingIntent = onClickPendingIntent,
            2,
            context
        )
    }

    fun showInvitationResponseNotification(invitation: InvitationEntity, title : String, contentText : String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY, invitation.id)
        }
        val onClickPendingIntent =
            PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        showNotification(
            title = title,
            contentText = contentText,
            pendingIntent = onClickPendingIntent,
            2,
            context
        )
    }


    fun showAccessUpdateNotification(invitation: InvitationEntity, access : String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY, invitation.id)
        }
        val onClickPendingIntent =
            PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        showNotification(
            title = "Project access updated to '$access'",
            contentText = "${invitation.inviteeName} has made you $access of project - ${invitation.projectName}.",
            pendingIntent = onClickPendingIntent,
            2,
            context
        )

    }



    companion object {
        const val CHANNEL_ID = "ChannelIDForInvitations"
        const val CHANNEL_NAME = "Invitation Request Notifications"
        const val CHANNEL_DESC = "Shows notification when someone send you a project invite."
        const val INVITATION_ID_KEY = "invitation_id_key"
    }
}