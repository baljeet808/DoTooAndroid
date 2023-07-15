package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DoTooItemDao {

    @Upsert
    suspend fun upsertAll(doTooItems : List<DoTooItemEntity>)

    @Query("SELECT * FROM todos WHERE projectId = :projectId")
    fun getAllDoTooItems(projectId: String) : Flow<List<DoTooItemEntity>>


    @Query("SELECT * FROM todos where id = :doTooId")
    suspend fun getDoTooById(doTooId : String) : DoTooItemEntity

    @Delete
    fun delete(doTooItem : DoTooItemEntity)

    @Query("SELECT * FROM todos WHERE dueDate = :yesterdayDateInLong")
    fun getYesterdayTasks(yesterdayDateInLong : Long): Flow<List<DoTooItemEntity>>

    @Query("SELECT * FROM todos WHERE dueDate = :todayDateInLong")
    fun getTodayTasks(todayDateInLong : Long): Flow<List<DoTooItemEntity>>

    @Query("SELECT * FROM todos WHERE dueDate = :tomorrowDateInLong")
    fun getTomorrowTasks(tomorrowDateInLong : Long): Flow<List<DoTooItemEntity>>

    @Query("SELECT * FROM todos WHERE dueDate < :yesterdayDateInLong")
    fun getPendingTasks(yesterdayDateInLong : Long): Flow<List<DoTooItemEntity>>

    @Query("SELECT * FROM todos WHERE dueDate > :tomorrowDateInLong")
    fun getAllOtherTasks(tomorrowDateInLong : Long): Flow<List<DoTooItemEntity>>

}