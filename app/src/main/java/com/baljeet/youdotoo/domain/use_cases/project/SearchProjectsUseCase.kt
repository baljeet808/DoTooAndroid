package com.baljeet.youdotoo.domain.use_cases.project

import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchProjectsUseCase @Inject constructor(
    private val repository: ProjectRepository
){
    suspend fun invoke(searchQuery: String ) :List<Project> {
        return repository.searchProjects(searchQuery)
    }
}