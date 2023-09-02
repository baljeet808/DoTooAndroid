package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class DoTooItemsRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : DoTooItemsRepository {

    private val doToosDao = localDB.doTooItemDao

    override fun getAllDoTooItems(): Flow<List<TaskEntity>> {
        return doToosDao.getAllDoTooItems()
    }

    override fun getAllTasksWithProjectAsFlow(): Flow<List<TaskWithProject>> {
        return doToosDao.getAllTasksWithProjectAsFlow()
    }

    override suspend fun getAllTasksWithProject(): List<TaskWithProject> {
        return doToosDao.getAllTasksWithProject()
    }

    override fun getAllDoTooItemsByProjectIDAsFlow(projectId: String): Flow<List<TaskEntity>> {
        return doToosDao.getAllDoTooItemsByProjectIDAsFlow(projectId = projectId)
    }
    override suspend fun getDoToosByProjectId(projectId: String): List<TaskEntity> {
        return doToosDao.getAllDoTooItemsByProjectID(projectId = projectId)
    }

    override suspend fun getDoTooById(doTooId: String): TaskEntity {
        return doToosDao.getDoTooById(doTooId)
    }

    override fun getTaskByIdAsAFlow(taskId: String): Flow<TaskEntity> {
        return doToosDao.getTaskByIdAsAFlow(taskId)
    }

    override fun getYesterdayTasks(yesterdayDateInLong: Long): Flow<List<TaskEntity>> {
        return doToosDao.getYesterdayTasks(yesterdayDateInLong)
    }

    override fun getTodayTasks(todayDateInLong: Long): Flow<List<TaskEntity>> {
        return doToosDao.getTodayTasks(todayDateInLong)
    }

    override fun getTomorrowTasks(tomorrowDateInLong: Long): Flow<List<TaskEntity>> {
        return doToosDao.getTomorrowTasks(tomorrowDateInLong)
    }

    override fun getPendingTasks(yesterdayDateInLong: Long): Flow<List<TaskEntity>> {
        return doToosDao.getPendingTasks(yesterdayDateInLong)
    }

    override fun getAllOtherTasks(tomorrowDateInLong: Long): Flow<List<TaskEntity>> {
        return doToosDao.getAllOtherTasks(tomorrowDateInLong)
    }

    override suspend fun upsertDoTooItem(tasks: List<TaskEntity>) {
        return doToosDao.upsertAll(tasks)
    }

    override suspend fun deleteDoTooItem(task: TaskEntity) {
        return doToosDao.delete(task)
    }

    override suspend fun deleteAllByProjectId(projectId: String) {
        return doToosDao.deleteAllByProjectId(projectId = projectId)
    }

}