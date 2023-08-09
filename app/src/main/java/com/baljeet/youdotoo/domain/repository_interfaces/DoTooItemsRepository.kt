package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import kotlinx.coroutines.flow.Flow

interface DoTooItemsRepository {

     fun getAllDoTooItems(): Flow<List<DoTooItemEntity>>

     fun getDoToosByProjectId(projectId : String): Flow<List<DoTooItemEntity>>

     suspend fun getDoTooById(doTooId : String): DoTooItemEntity

     fun getTaskByIdAsAFlow(taskId : String): Flow<DoTooItemEntity>

     fun getYesterdayTasks(yesterdayDateInLong: Long): Flow<List<DoTooItemEntity>>

     fun getTodayTasks(todayDateInLong: Long): Flow<List<DoTooItemEntity>>


     fun getTomorrowTasks(tomorrowDateInLong: Long): Flow<List<DoTooItemEntity>>

     fun getPendingTasks(yesterdayDateInLong: Long): Flow<List<DoTooItemEntity>>

     fun getAllOtherTasks(tomorrowDateInLong: Long ): Flow<List<DoTooItemEntity>>

     suspend fun upsertDoTooItem(doTooItems : List<DoTooItem>, projectId: String)

     suspend fun deleteDoTooItem(doTooItem : DoTooItem, projectId: String)

     suspend fun deleteAllByProjectId(projectId: String)

}