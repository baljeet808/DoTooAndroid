package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity


@Dao
interface UserDao {

    @Upsert
    suspend fun upsertAll(users : List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<UserEntity>

    @Query("SELECT * FROM users where id = :userId ")
    suspend fun getUserById(userId : String) : UserEntity?

    @Delete
    fun delete(user : UserEntity)

}