package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.domain.models.Project


interface ProjectRepository {
     suspend fun getProjects(): List<Project>
     suspend fun upsertProject(projects : List<Project>)
     suspend fun deleteProject(project : Project)
     suspend fun searchProjects(searchQuery : String) : List<Project>
}