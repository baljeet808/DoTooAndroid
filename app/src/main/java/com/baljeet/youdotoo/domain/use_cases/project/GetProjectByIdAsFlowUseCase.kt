package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProjectByIdAsFlowUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    operator fun invoke(projectId : String): Flow<ProjectEntity> {
        return repository.getProjectByIdAsFlow(projectId)
    }
}