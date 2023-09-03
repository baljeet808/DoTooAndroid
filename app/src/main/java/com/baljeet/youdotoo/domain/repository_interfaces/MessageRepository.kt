package com.baljeet.youdotoo.domain.repository_interfaces

import androidx.paging.PagingSource
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import kotlinx.coroutines.flow.Flow


interface MessageRepository {
     suspend fun upsertAll(messages : List<MessageEntity>)
     fun getAllMessagesAsFlow() : Flow<List<MessageEntity>>
     suspend fun getAllMessages() : List<MessageEntity>
     suspend fun getMessageById(messageId: String) : MessageEntity?
     fun getMessageByIdAsFlow(messageId: String) : Flow<MessageEntity?>
     fun getAllMessagesOfAProjectAsFlow(projectId: String) : Flow<List<MessageEntity>>
     suspend fun getAllMessagesOfAProject(projectId: String) : List<MessageEntity>

     fun getAllMessageByProjectId(projectId: String): PagingSource<Int, MessageEntity>

     suspend fun delete(message: MessageEntity)
     suspend fun deleteAllMessagesOfAProject(projectId: String)
}