package com.baljeet.youdotoo.domain.use_cases.notifications

import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.domain.repository_interfaces.NotificationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllNotificationsAsFlowUseCase @Inject constructor(
    private val repository: NotificationRepository
){
    operator fun invoke(): Flow<List<NotificationEntity>> {
        return repository.getAllNotificationsAsFlow()
    }
}