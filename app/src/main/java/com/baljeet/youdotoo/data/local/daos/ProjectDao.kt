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
    suspend fun getAllClients() : List<ProjectEntity>

    @Delete
    fun delete(project : ProjectEntity)

    @Query("SELECT * FROM projects WHERE name LIKE :searchQuery ")
    fun search(searchQuery : String) : List<ProjectEntity>

}