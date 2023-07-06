package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.converters.convertLocalDateTimeToEpochSeconds
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject


class DoTooItemsRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : DoTooItemsRepository {

    private val doToosDao = localDB.doTooItemDao

    override suspend fun getDoToos(projectId: String): List<DoTooItem> {
        return doToosDao.getAllDoTooItems(projectId = projectId).map { it.toDoTooItem() }
    }

    override suspend fun getTodayTasks(): List<DoTooItem> {
        val todayDateInLong = java.time.LocalDateTime.now().toKotlinLocalDateTime()
        val todayStartDateTime = LocalDateTime(
            year = todayDateInLong.year,
            monthNumber = todayDateInLong.monthNumber,
            dayOfMonth = todayDateInLong.dayOfMonth,
            hour = 0,
            minute = 0
        )
        val todayEndDateTime = LocalDateTime(
            year = todayDateInLong.year,
            monthNumber = todayDateInLong.monthNumber,
            dayOfMonth = todayDateInLong.dayOfMonth,
            hour = 23,
            minute = 59,
            second = 59
        )
        return doToosDao.getTodayTasks(
            startTimeDate = convertLocalDateTimeToEpochSeconds(todayStartDateTime),
            endTimeDate = convertLocalDateTimeToEpochSeconds(todayEndDateTime)
        ).map { it.toDoTooItem() }
    }

    override suspend fun upsertDoTooItem(doTooItems: List<DoTooItem>, projectId: String) {
        return doToosDao.upsertAll(doTooItems.map { it.toDoTooItemEntity(projectId) })
    }

    override suspend fun deleteDoTooItem(doTooItem: DoTooItem, projectId: String) {
        return doToosDao.delete(doTooItem.toDoTooItemEntity(projectId))
    }

}