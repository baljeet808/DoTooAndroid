package com.baljeet.youdotoo.domain.use_cases.messages

import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.repository_interfaces.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllMessagesAsFlowUseCase @Inject constructor(
    private val repository: MessageRepository
){
    operator fun invoke() : Flow<List<MessageEntity>> {
        return repository.getAllMessagesAsFlow()
    }
}