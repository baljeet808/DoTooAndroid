package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.Project
import kotlinx.coroutines.flow.Flow


interface ProjectRepository {
     fun getProjects(): Flow<List<ProjectEntity>>
     fun getAllProjectsAndTasksAsFlow(): Flow<List<ProjectWithDoToos>>
     suspend fun getProjectById(projectId : String): ProjectEntity
     fun getProjectByIdAsFlow(projectId : String): Flow<ProjectEntity>
     suspend fun upsertProject(projects : List<Project>)
     suspend fun deleteProject(project : Project)
     suspend fun searchProjects(searchQuery : String) : List<Project>
}