package com.baljeet.youdotoo.services

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.baljeet.youdotoo.MainActivity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InvitationNotificationService @Inject constructor(
    @ApplicationContext val context: Context,
) : DoTooNotification() {


    fun showInviteNotification(invitation: InvitationEntity) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY, invitation.id)
        }
        val onClickPendingIntent =
            PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        showNotification(
            title = "Project Invitation.",
            contentText = "${invitation.inviteeName} has invited you to a project : ' ${invitation.projectName} '. See more details about project in app.",
            pendingIntent = onClickPendingIntent,
            1,
            context
        )
    }

    fun showInvitationResponseNotification(invitation: InvitationEntity, status : String) {
        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(INVITATION_ID_KEY, invitation.id)
        }
        val onClickPendingIntent =
            PendingIntent.getActivity(context, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

        showNotification(
            title = "Invitation $status.",
            contentText = "${invitation.invitedEmail} has $status your invitation.",
            pendingIntent = onClickPendingIntent,
            1,
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
            1,
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