package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.User
import kotlinx.coroutines.flow.Flow


interface UserRepository {
     fun getUsers(): Flow<List<UserEntity>>
     suspend fun getUserById(userId : String): User?
     fun getUserByIdAsAFlow(userId : String): Flow<UserEntity?>
     suspend fun upsertUsers(users : List<User>)
     suspend fun deleteUser(user : User)
     fun getAllUsersOfTheseIds(ids : List<String>) : Flow<List<UserEntity>>
}