package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
     suspend fun getUsers(): List<User>
     suspend fun getUserById(userId : String): User?
     suspend fun upsertUsers(users : List<User>)
     suspend fun deleteUser(user : User)
     fun getAllUsersOfTheseIds(ids : List<String>) : Flow<List<UserEntity>>
}