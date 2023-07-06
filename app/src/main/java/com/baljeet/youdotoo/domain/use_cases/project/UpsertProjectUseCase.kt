package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpsertProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    suspend fun invoke(projects: List<Project> ) {
        return repository.upsertProject(projects)
    }
}