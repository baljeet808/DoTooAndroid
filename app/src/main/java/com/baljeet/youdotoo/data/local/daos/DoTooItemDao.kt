package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity


@Dao
interface DoTooItemDao {

    @Upsert
    suspend fun upsertAll(doTooItems : List<DoTooItemEntity>)

    @Query("SELECT * FROM todos WHERE projectId = :projectId")
    suspend fun getAllDoTooItems(projectId: String) : List<DoTooItemEntity>

    @Delete
    fun delete(doTooItem : DoTooItemEntity)

    @Query("SELECT * FROM todos WHERE dueDate >= :startTimeDate && dueDate <= :endTimeDate")
    suspend fun getTodayTasks(startTimeDate : Long, endTimeDate : Long): List<DoTooItemEntity>

}