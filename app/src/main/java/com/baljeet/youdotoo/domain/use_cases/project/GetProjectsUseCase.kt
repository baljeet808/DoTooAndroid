package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    suspend operator fun invoke(): List<Project>  {
        return repository.getProjects()
    }
}