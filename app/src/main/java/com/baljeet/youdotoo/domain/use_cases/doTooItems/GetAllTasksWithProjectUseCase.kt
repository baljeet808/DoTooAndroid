package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllTasksWithProjectUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    suspend operator fun invoke(): List<TaskWithProject> {
        return repository.getAllTasksWithProject()
    }
}