package com.baljeet.youdotoo.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.baljeet.youdotoo.MainActivity
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.ConstSampleAvatarUrl
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationsService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if(token.isNotBlank()) {
            SharedPref.firebaseToken = token
            val db = FirebaseFirestore.getInstance()

            Firebase.auth.currentUser?.let {
                val newUser = User(
                    id = it.uid,
                    name = it.displayName ?: "Unknown",
                    email = it.email ?: "Not given",
                    joined = SharedPref.userJoined,
                    avatarUrl = it.photoUrl?.toString() ?: ConstSampleAvatarUrl,
                    firebaseToken = token
                )

                db.collection("users")
                    .document(newUser.id)
                    .set(newUser)
                    .addOnSuccessListener {
                        Log.d("Log for - ", "New token assigned to user.")
                    }.addOnFailureListener {
                        Log.d("Log for - ", "Failed to upload to new token to user profile.")
                    }
            } ?: kotlin.run {
                Log.d("Log for - ", "Failed to upload to new token to user profile. null auth")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        var notificationType = EnumNotificationType.valueOf(data[MESSAGE_TYPE] ?:"General")

        val projectId = data[PROJECT_ID]?:""
        val projectColor = data[PROJECT_COLOR]?:0L
        val senderId = data[SENDER_ID]?:""
        val senderAvatar = data[SENDER_AVATAR_URL]?:""
        val senderName = data[SENDER_NAME]?:""
        val projectDetail = data[PROJECT_DETAIL]?:""
        val projectName = data[PROJECT_NAME]?:""
        val invitedEmail = data[INVITED_EMAIL]?:""

        message.notification?.let { notificationData ->

            val mainActivityIntent = Intent(this, MainActivity::class.java).apply {
                putExtra(PROJECT_ID, notificationData.title)
            }
            val onClickPendingIntent =
                PendingIntent.getActivity(this, 1, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            Log.d("Log for -", "firebase notification received")
            Log.d("Log for -", "firebase notification title ${notificationData.title}")

            val notification = NotificationCompat.Builder(
                this,
                CHANNEL_ID
            )
                .setSmallIcon(R.drawable.youdotoo_app_icon)
                .setContentTitle(notificationData.title)
                .setContentText(notificationData.body)
                .setStyle(NotificationCompat.BigTextStyle())
                .setContentIntent(onClickPendingIntent)
                .build()

            notificationManager.notify(
                88,
                notification
            )
        }
    }

    companion object {
        const val CHANNEL_ID = "ChannelIDForInvitations"
        const val PROJECT_ID = "projectId"
        const val MESSAGE_TYPE = "messageType"
        const val SENDER_ID = "senderId"
        const val PROJECT_COLOR = "projectColor"
        const val SENDER_AVATAR_URL = "senderAvatarUrl"
        const val SENDER_NAME = "senderName"
        const val PROJECT_DETAIL = "projectDetail"
        const val PROJECT_NAME = "projectName"
        const val INVITED_EMAIL = "invitedEmail"

    }
}