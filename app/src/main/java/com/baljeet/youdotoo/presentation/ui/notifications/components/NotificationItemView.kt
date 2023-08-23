package com.baljeet.youdotoo.presentation.ui.notifications.components

import androidx.compose.runtime.Composable
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.data.local.entities.NotificationEntity

@Composable
fun NotificationItemView(
    notification: NotificationEntity,
    onItemClick: () -> Unit
) {

    when (notification.notificationType) {
        EnumNotificationType.NewInvitation -> {
            InvitationNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.ProjectUpdate -> {
            ProjectUpdateNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.TaskUpdate -> {
            TaskUpdateNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.NewMessage -> {
            MessageNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.General -> {
            GeneralNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }
        else -> {
            GeneralNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }
    }
}

