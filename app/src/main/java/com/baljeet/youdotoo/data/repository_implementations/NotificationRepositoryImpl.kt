package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Updated by Baljeet singh.
 * **/
class NotificationRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : NotificationRepository {

    private val notificationDao = localDB.notificationDao

    override fun getAllNotificationsAsFlow(): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotificationsAsFlow()
    }

    override fun getAllNotificationsByProjectIDAsFlow(projectId: String): Flow<List<NotificationEntity>> {
        return notificationDao.getAllNotificationsByProjectIDAsFlow(projectId)
    }

    override suspend fun upsertAllNotifications(notifications: List<NotificationEntity>) {
        notificationDao.upsertAll(notifications)
    }

    override suspend fun getNotificationById(notificationId: String): NotificationEntity {
        return notificationDao.getNotificationById(notificationId)
    }

    override suspend fun deleteNotification(notification: NotificationEntity) {
        return notificationDao.delete(notification)
    }

}