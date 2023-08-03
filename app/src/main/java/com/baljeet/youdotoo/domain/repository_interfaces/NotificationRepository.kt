package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow


interface NotificationRepository {
     fun getAllNotificationsAsFlow(): Flow<List<NotificationEntity>>
     fun getAllNotificationsByProjectIDAsFlow(projectId : String): Flow<List<NotificationEntity>>
     suspend fun upsertAllNotifications(notifications : List<NotificationEntity>)
     suspend fun getNotificationById(notificationId : String): NotificationEntity
     suspend fun deleteNotification(notification : NotificationEntity)
     suspend fun deleteAllNotifications()
}