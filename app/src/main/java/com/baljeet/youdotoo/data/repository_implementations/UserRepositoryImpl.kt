package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.data.mappers.toUser
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.repository_interfaces.ProjectRepository
import com.baljeet.youdotoo.domain.repository_interfaces.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : UserRepository {

    private val userDao = localDB.userDao

    override suspend fun getUsers(): List<User> {
        return userDao.getAllUsers().map { it.toUser() }
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

}