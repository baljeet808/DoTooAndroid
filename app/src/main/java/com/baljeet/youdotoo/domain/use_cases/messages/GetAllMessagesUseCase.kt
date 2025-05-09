package com.baljeet.youdotoo.domain.use_cases.messages

import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.repository_interfaces.MessageRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
){
    suspend operator fun invoke() : List<MessageEntity> {
        return repository.getAllMessages()
    }
}