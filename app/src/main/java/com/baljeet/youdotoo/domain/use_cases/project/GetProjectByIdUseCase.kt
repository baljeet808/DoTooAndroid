package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProjectByIdUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    suspend operator fun invoke(projectId : String): ProjectEntity {
        return repository.getProjectById(projectId)
    }
}