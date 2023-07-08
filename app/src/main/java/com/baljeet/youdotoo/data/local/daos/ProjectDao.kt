package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.ProjectEntity


@Dao
interface ProjectDao {

    @Upsert
    suspend fun upsertAll(projects : List<ProjectEntity>)

    @Query("SELECT * FROM projects")
    suspend fun getAllProjects() : List<ProjectEntity>

    @Query("SELECT * FROM projects where id = :projectId")
    suspend fun getProjectById(projectId : String) : ProjectEntity

    @Delete
    suspend fun delete(project : ProjectEntity)

    @Query("SELECT * FROM projects WHERE name LIKE :searchQuery ")
    suspend fun search(searchQuery : String) : List<ProjectEntity>

}