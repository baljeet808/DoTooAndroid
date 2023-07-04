package com.baljeet.youdotoo.data.mappers

import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.models.Message


fun Message.toMessageEntity(doTooId : String, projectId : String): MessageEntity{
    return MessageEntity(
        id = id,
        doTooId = doTooId,
        projectId = projectId,
        message = message,
        senderId = senderId,
        createdAt = createdAt,
        isUpdate = isUpdate,
        attachmentUrl = attachmentUrl,
        interactions = interactions.joinToString("|")
    )
}


fun MessageEntity.toMessage(): Message{
    return Message(
        id = id,
        message = message,
        senderId = senderId,
        createdAt = createdAt,
        isUpdate = isUpdate,
        attachmentUrl = attachmentUrl,
        interactions = interactions.split("|").toCollection(ArrayList())
    )
}

