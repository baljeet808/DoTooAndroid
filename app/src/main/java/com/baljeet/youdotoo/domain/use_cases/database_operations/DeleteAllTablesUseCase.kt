package com.baljeet.youdotoo.domain.use_cases.database_operations

import com.baljeet.youdotoo.domain.repository_interfaces.DatabaseOperationsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeleteAllTablesUseCase @Inject constructor(
    private val repository: DatabaseOperationsRepository
){
    suspend operator fun invoke()  {
        return repository.deleteAllTables()
    }
}