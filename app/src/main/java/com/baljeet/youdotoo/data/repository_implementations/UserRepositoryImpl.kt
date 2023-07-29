package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toUser
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.repository_interfaces.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : UserRepository {

    private val userDao = localDB.userDao

    override fun getUsers(): Flow<List<UserEntity>> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)?.toUser()
    }

    override suspend fun upsertUsers(users: List<User>) {
        userDao.upsertAll(users = users.map { it.toUserEntity() })
    }

    override suspend fun deleteUser(user: User) {
        userDao.delete(user.toUserEntity())
    }

    override fun getAllUsersOfTheseIds(ids: List<String>): Flow<List<UserEntity>> {
        return userDao.getAllUsersOfTheseIds(ids)
    }

}