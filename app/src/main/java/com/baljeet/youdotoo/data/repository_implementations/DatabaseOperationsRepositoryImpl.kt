package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.DatabaseOperationsRepository
import javax.inject.Inject


class DatabaseOperationsRepositoryImpl @Inject constructor(
    private val localDB: YouDoTooDatabase
) : DatabaseOperationsRepository {
    override suspend fun deleteAllTables() {
        localDB.clearAllTables()
    }
}