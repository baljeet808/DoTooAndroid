package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import javax.inject.Inject


class DoTooItemsRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : DoTooItemsRepository {

    private val doToosDao = localDB.doTooItemDao

    override suspend fun getDoToos(projectId: String): List<DoTooItem> {
        return doToosDao.getAllDoTooItems(projectId = projectId).map { it.toDoTooItem() }
    }

    override suspend fun getTodayTasks(todayDateInLong: Long): List<DoTooItem> {
        return doToosDao.getTodayTasks(todayDateInLong).map { it.toDoTooItem() }
    }

    override suspend fun upsertDoTooItem(doTooItems: List<DoTooItem>, projectId: String) {
        return doToosDao.upsertAll(doTooItems.map { it.toDoTooItemEntity(projectId) })
    }

    override suspend fun deleteDoTooItem(doTooItem: DoTooItem, projectId: String) {
        return doToosDao.delete(doTooItem.toDoTooItemEntity(projectId))
    }

}