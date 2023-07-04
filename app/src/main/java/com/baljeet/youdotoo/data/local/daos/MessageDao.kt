package com.baljeet.youdotoo.data.local.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.baljeet.youdotoo.data.local.entities.MessageEntity


@Dao
interface MessageDao {

    @Upsert
    suspend fun upsertAll(messages : List<MessageEntity>)

    @Query("SELECT * FROM messages where doTooId == :doTooId")
    suspend fun getAllMessages(doTooId : String) : List<MessageEntity>

    @Delete
    fun delete(message : MessageEntity)

}