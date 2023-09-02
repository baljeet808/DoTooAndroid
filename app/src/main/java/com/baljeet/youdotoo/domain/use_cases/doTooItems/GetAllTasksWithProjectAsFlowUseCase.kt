package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllTasksWithProjectAsFlowUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    operator fun invoke(): Flow<List<TaskWithProject>> {
        return repository.getAllTasksWithProjectAsFlow()
    }
}