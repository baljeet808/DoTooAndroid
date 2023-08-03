package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface NotificationDao {

    @Upsert
    suspend fun upsertAll(notifications : List<NotificationEntity>)

    @Query("SELECT * FROM notifications WHERE projectId = :projectId")
    fun getAllNotificationsByProjectIDAsFlow(projectId: String) : Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications")
    fun getAllNotificationsAsFlow() : Flow<List<NotificationEntity>>


    @Query("SELECT * FROM notifications where id = :notificationId")
    suspend fun getNotificationById(notificationId : String) : NotificationEntity

    @Delete
    fun delete(notification : NotificationEntity)
    @Query("Delete FROM notifications")
    suspend fun deleteAllNotifications()

}