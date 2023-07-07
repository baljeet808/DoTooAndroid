package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.domain.models.DoTooItem

interface DoTooItemsRepository {
     suspend fun getDoToos(projectId : String): List<DoTooItem>
     suspend fun getTodayTasks(startTimeDate: Long, endTimeDate : Long): List<DoTooItem>
     suspend fun upsertDoTooItem(doTooItems : List<DoTooItem>, projectId: String)
     suspend fun deleteDoTooItem(doTooItem : DoTooItem, projectId: String)
}