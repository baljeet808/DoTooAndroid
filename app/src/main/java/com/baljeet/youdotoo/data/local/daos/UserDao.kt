package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Upsert
    suspend fun upsertAll(users : List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getAllUsers() : Flow<List<UserEntity>>

    @Query("SELECT * FROM users where id = :userId ")
    suspend fun getUserById(userId : String) : UserEntity?
    @Query("SELECT * FROM users where id = :userId ")
    fun getUserByIdAsAFlow(userId : String) : Flow<UserEntity?>

    @Query("SELECT * FROM users where id IN (:userIds)")
    fun getAllUsersOfTheseIds(userIds : List<String>) : Flow<List<UserEntity>>

    @Delete
    fun delete(user : UserEntity)

}