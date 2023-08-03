package com.baljeet.youdotoo.domain.repository_interfaces

interface DatabaseOperationsRepository {

     suspend fun deleteAllTables()

}