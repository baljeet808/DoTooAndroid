package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import kotlinx.coroutines.flow.Flow

interface DoTooItemsRepository {
     fun getAllDoTooItems(): Flow<List<TaskEntity>>
     fun getAllTasksWithProjectAsFlow(): Flow<List<TaskWithProject>>
     suspend fun getAllTasksWithProject(): List<TaskWithProject>
     suspend fun getDoToosByProjectId(projectId : String): List<TaskEntity>
     fun getAllDoTooItemsByProjectIDAsFlow(projectId : String): Flow<List<TaskEntity>>
     suspend fun getDoTooById(doTooId : String): TaskEntity
     fun getTaskByIdAsAFlow(taskId : String): Flow<TaskEntity>
     fun getYesterdayTasks(yesterdayDateInLong: Long): Flow<List<TaskEntity>>
     fun getTodayTasks(todayDateInLong: Long): Flow<List<TaskEntity>>
     fun getTomorrowTasks(tomorrowDateInLong: Long): Flow<List<TaskEntity>>
     fun getPendingTasks(yesterdayDateInLong: Long): Flow<List<TaskEntity>>
     fun getAllOtherTasks(tomorrowDateInLong: Long ): Flow<List<TaskEntity>>
     suspend fun upsertDoTooItem(tasks : List<TaskEntity>)
     suspend fun deleteDoTooItem(task : TaskEntity)
     suspend fun deleteAllByProjectId(projectId: String)
}