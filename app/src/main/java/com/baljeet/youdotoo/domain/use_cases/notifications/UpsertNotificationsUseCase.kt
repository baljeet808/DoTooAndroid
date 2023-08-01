package com.baljeet.youdotoo.domain.use_cases.notifications

import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.domain.repository_interfaces.NotificationRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpsertNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
){
    suspend operator fun invoke(notifications: List<NotificationEntity> ) {
        return repository.upsertAllNotifications(notifications)
    }
}