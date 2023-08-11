package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class MessageRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : MessageRepository {

    private val messageDao = localDB.messageDao
    override suspend fun upsertAll(messages: List<MessageEntity>) {
        messageDao.upsertAll(messages)
    }

    override fun getAllMessagesAsFlow(): Flow<List<MessageEntity>> {
        return messageDao.getAllMessagesAsFlow()
    }

    override suspend fun getAllMessages(): List<MessageEntity> {
        return messageDao.getAllMessages()
    }

    override suspend fun getMessageById(messageId: String): MessageEntity? {
        return messageDao.getMessageById(messageId = messageId)
    }

    override fun getAllMessagesOfAProjectAsFlow(projectId: String): Flow<List<MessageEntity>> {
        return messageDao.getAllMessagesOfAProjectAsFlow(projectId)
    }

    override suspend fun getAllMessagesOfAProject(projectId: String): List<MessageEntity> {
        return messageDao.getAllMessagesOfAProject(projectId)
    }

    override suspend fun delete(message: MessageEntity) {
        messageDao.delete(message)
    }

    override suspend fun deleteAllMessagesOfAProject(projectId: String) {
        messageDao.deleteAllMessagesOfAProject(projectId)
    }


}