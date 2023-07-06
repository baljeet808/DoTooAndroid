package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import javax.inject.Inject

/**
 * Updated by Baljeet singh.
 * **/
class ProjectRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : ProjectRepository {

    val projectDao = localDB.projectDao

    override suspend fun getProjects(): List<Project> {
        return projectDao.getAllProjects().map { it.toProject() }
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