package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Updated by Baljeet singh.
 * **/
class ProjectRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : ProjectRepository {

    private val projectDao = localDB.projectDao

    override fun getProjects(): Flow<List<ProjectEntity>> {
        return projectDao.getAllProjects()
    }

    override fun getAllProjectsAndTasksAsFlow(): Flow<List<ProjectWithDoToos>> {
        return projectDao.getAllProjectsAndTasksAsFlow()
    }

    override suspend fun getProjectById(projectId: String): ProjectEntity {
        return projectDao.getProjectById(projectId)
    }

    override fun getProjectByIdAsFlow(projectId: String): Flow<ProjectEntity> {
        return projectDao.getProjectByIdAsFlow(projectId)
    }

    override suspend fun upsertProject(projects: List<Project>) {
        projectDao.upsertAll(projects.map { it.toProjectEntity() })
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.delete(project.toProjectEntity())
    }

    override suspend fun searchProjects(searchQuery: String): List<Project> {
        return projectDao.search(searchQuery).map { it.toProject() }
    }
}