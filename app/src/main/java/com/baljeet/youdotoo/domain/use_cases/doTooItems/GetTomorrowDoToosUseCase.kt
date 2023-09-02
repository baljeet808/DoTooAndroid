package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetTomorrowDoToosUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    operator fun invoke(tomorrowDateInLong: Long): Flow<List<TaskEntity>> {
        return repository.getTomorrowTasks(tomorrowDateInLong)
    }
}