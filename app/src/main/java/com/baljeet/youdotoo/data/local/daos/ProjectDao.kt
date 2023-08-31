package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import kotlinx.coroutines.flow.Flow


@Dao
interface ProjectDao {

    @Upsert
    suspend fun upsertAll(projects : List<ProjectEntity>)

    @Query("SELECT * FROM projects")
    fun getAllProjectsAsFlow() : Flow<List<ProjectEntity>>
    @Query("SELECT * FROM projects")
    suspend fun getAllProjects() : List<ProjectEntity>

    @Query("SELECT * FROM projects where id = :projectId")
    suspend fun getProjectById(projectId : String) : ProjectEntity

    @Query("SELECT * FROM projects where id = :projectId")
    fun getProjectByIdAsFlow(projectId : String) : Flow<ProjectEntity>

    @Query("SELECT * FROM projects")
    fun getAllProjectsAndTasksAsFlow() : Flow<List<ProjectWithDoToos>>

    @Delete
    suspend fun delete(project : ProjectEntity)

    @Query("SELECT * FROM projects WHERE name LIKE :searchQuery ")
    suspend fun search(searchQuery : String) : List<ProjectEntity>

}