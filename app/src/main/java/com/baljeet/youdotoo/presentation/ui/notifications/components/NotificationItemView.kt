package com.baljeet.youdotoo.presentation.ui.notifications.components

import androidx.compose.runtime.Composable
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.data.local.entities.NotificationEntity

@Composable
fun NotificationItemView(
    notification: NotificationEntity,
    onDeleteNotification: () -> Unit,
    onItemClick: () -> Unit
) {

    val notificationType = if (notification.messageId != null) {
        EnumNotificationType.MESSAGE
    } else if (notification.taskId != null) {
        EnumNotificationType.TASK_UPDATE
    } else if (notification.projectId != null) {
        EnumNotificationType.PROJECT_UPDATE
    } else if (notification.invitationId != null) {
        EnumNotificationType.INVITATION
    } else {
        EnumNotificationType.GENERAL
    }


    when (notificationType) {
        EnumNotificationType.INVITATION -> {
            InvitationNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.PROJECT_UPDATE -> {
            ProjectUpdateNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.TASK_UPDATE -> {
            TaskUpdateNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.MESSAGE -> {
            MessageNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }

        EnumNotificationType.GENERAL -> {
            GeneralNotificationItemView(
                notification = notification,
                onItemClick = onItemClick
            )
        }
    }
}

