package com.baljeet.youdotoo.domain.use_cases.messages

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.repository_interfaces.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetPagedMessagesOfProject @Inject constructor(
    private val repository: MessageRepository
){
    operator fun invoke(projectId : String) : Flow<PagingData<MessageEntity>> = Pager(
        PagingConfig(
            pageSize = 20,
            prefetchDistance = 10
        )
    ){
        repository.getAllMessageByProjectId(projectId)
    }.flow
}