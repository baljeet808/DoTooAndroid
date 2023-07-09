package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import kotlinx.coroutines.flow.Flow

interface DoTooItemsRepository {
     fun getDoToos(projectId : String): Flow<List<DoTooItemEntity>>
     suspend fun getDoTooById(doTooId : String): DoTooItemEntity
     fun getTodayTasks(todayDateInLong: Long): Flow<List<DoTooItemEntity>>
     suspend fun upsertDoTooItem(doTooItems : List<DoTooItem>, projectId: String)
     suspend fun deleteDoTooItem(doTooItem : DoTooItem, projectId: String)
}