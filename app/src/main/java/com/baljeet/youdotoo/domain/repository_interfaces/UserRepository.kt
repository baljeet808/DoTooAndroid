package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.domain.models.User


interface UserRepository {
     suspend fun getUsers(): List<User>
     suspend fun getUserById(userId : String): User?
     suspend fun upsertUsers(users : List<User>)
     suspend fun deleteUser(user : User)
}