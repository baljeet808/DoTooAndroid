package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProjectsWithDoToosUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    operator fun invoke(): Flow<List<ProjectWithDoToos>> {
        return repository.getAllProjectsAndTasksAsFlow()
    }
}