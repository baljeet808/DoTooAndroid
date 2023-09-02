package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import kotlinx.coroutines.flow.Flow


@Dao
interface DoTooItemDao {

    @Upsert
    suspend fun upsertAll(doTooItems : List<TaskEntity>)

    @Query("SELECT * FROM todos WHERE projectId = :projectId")
    suspend fun getAllDoTooItemsByProjectID(projectId: String) : List<TaskEntity>
    @Query("SELECT * FROM todos WHERE projectId = :projectId")
    fun getAllDoTooItemsByProjectIDAsFlow(projectId: String) : Flow<List<TaskEntity>>

    @Query("SELECT * FROM todos")
    fun getAllDoTooItems() : Flow<List<TaskEntity>>
    @Query("SELECT * FROM todos")
    fun getAllTasksWithProjectAsFlow() : Flow<List<TaskWithProject>>
    @Query("SELECT * FROM todos")
    suspend fun getAllTasksWithProject() : List<TaskWithProject>

    @Query("SELECT * FROM todos where id = :doTooId")
    suspend fun getDoTooById(doTooId : String) : TaskEntity

    @Query("SELECT * FROM todos where id = :taskId")
    fun getTaskByIdAsAFlow(taskId : String) : Flow<TaskEntity>

    @Delete
    fun delete(doTooItem : TaskEntity)

    @Query("DELETE FROM todos where projectId = :projectId")
    fun deleteAllByProjectId(projectId : String)

    @Query("SELECT * FROM todos WHERE dueDate = :yesterdayDateInLong")
    fun getYesterdayTasks(yesterdayDateInLong : Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM todos WHERE dueDate = :todayDateInLong")
    fun getTodayTasks(todayDateInLong : Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM todos WHERE dueDate = :tomorrowDateInLong")
    fun getTomorrowTasks(tomorrowDateInLong : Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM todos WHERE dueDate < :yesterdayDateInLong")
    fun getPendingTasks(yesterdayDateInLong : Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM todos WHERE dueDate > :tomorrowDateInLong")
    fun getAllOtherTasks(tomorrowDateInLong : Long): Flow<List<TaskEntity>>

}