package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import kotlinx.parcelize.Parcelize





@Parcelize
data class Message(
    var id: String,
    var senderId: String,
    var message: String,
    var createdAt: Long,
    var isUpdate: Boolean,
    var attachmentUrl: String?,
    var interactions: String,
    var projectId : String
) : Parcelable


fun Message.toMessageEntity(): MessageEntity{
    return MessageEntity(
        id = id,
        senderId = senderId,
        message = message,
        createdAt = createdAt,
        isUpdate = isUpdate,
        attachmentUrl = attachmentUrl,
        interactions = interactions,
        projectId = projectId
    )
}

fun MessageEntity.toMessage(): Message{
    return Message(
        id = id,
        senderId = senderId,
        message = message,
        createdAt = createdAt,
        isUpdate = isUpdate,
        attachmentUrl = attachmentUrl,
        interactions = interactions,
        projectId = projectId
    )
}




fun String.getInteractions(): ArrayList<Interaction> {
    val interactions = arrayListOf<Interaction>()
    this.split(" | ").forEach { interactionString ->
        val interactionArray = interactionString.split(",")
        interactions.add(
            Interaction(
                userId = interactionArray[0],
                emoticonName = interactionArray[1]
            )
        )
    }
    return interactions
}

fun Message.updateInteraction(interactionName: String) {
    val interactionId = SharedPref.userId.plus(",").plus(interactionName)
    val interactions =  this.interactions.split(" | ").toCollection(ArrayList())
    if (interactions.any { i -> i == interactionId }) {
        interactions.remove(interactionId)
    } else {
        interactions.add(interactionId)
    }
    this.interactions = interactions.joinToString(" | ")
}