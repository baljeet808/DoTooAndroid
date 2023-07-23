package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DoTooItemsRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : DoTooItemsRepository {

    private val doToosDao = localDB.doTooItemDao

    override fun getAllDoTooItems(): Flow<List<DoTooItemEntity>> {
        return doToosDao.getAllDoTooItems()
    }

    override fun getDoToosByProjectId(projectId: String): Flow<List<DoTooItemEntity>> {
        return doToosDao.getAllDoTooItemsByProjectID(projectId = projectId)
    }

    override suspend fun getDoTooById(doTooId: String): DoTooItemEntity {
        return doToosDao.getDoTooById(doTooId)
    }

    override fun getYesterdayTasks(yesterdayDateInLong: Long): Flow<List<DoTooItemEntity>> {
        return doToosDao.getYesterdayTasks(yesterdayDateInLong)
    }

    override fun getTodayTasks(todayDateInLong: Long): Flow<List<DoTooItemEntity>> {
        return doToosDao.getTodayTasks(todayDateInLong)
    }

    override fun getTomorrowTasks(tomorrowDateInLong: Long): Flow<List<DoTooItemEntity>> {
        return doToosDao.getTomorrowTasks(tomorrowDateInLong)
    }

    override fun getPendingTasks(yesterdayDateInLong: Long): Flow<List<DoTooItemEntity>> {
        return doToosDao.getPendingTasks(yesterdayDateInLong)
    }

    override fun getAllOtherTasks(tomorrowDateInLong: Long): Flow<List<DoTooItemEntity>> {
        return doToosDao.getAllOtherTasks(tomorrowDateInLong)
    }

    override suspend fun upsertDoTooItem(doTooItems: List<DoTooItem>, projectId: String) {
        return doToosDao.upsertAll(doTooItems.map { it.toDoTooItemEntity(projectId) })
    }

    override suspend fun deleteDoTooItem(doTooItem: DoTooItem, projectId: String) {
        return doToosDao.delete(doTooItem.toDoTooItemEntity(projectId))
    }

    override suspend fun deleteAllByProjectId(projectId: String) {
        return doToosDao.deleteAllByProjectId(projectId = projectId)
    }

}