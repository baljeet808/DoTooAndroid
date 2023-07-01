package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import com.baljeet.youdotoo.common.SharedPref
import kotlinx.parcelize.Parcelize

/**
 * Updated by Baljeet singh on 18th June,2023 at 1:10PM.
 * **/


class MessageDto {
    var id: String = ""
    var senderId: String = ""
    var message: String = ""
    var createdAt: Long = 0
    var isUpdate: Boolean  = false
    var attachmentUrl: String? = null
    var interactions: List<String> = listOf()
}

@Parcelize
data class Message(
    var id: String,
    var senderId: String,
    var message: String,
    var createdAt: Long,
    var isUpdate: Boolean,
    var attachmentUrl: String?,
    var interactions: List<String>
) : Parcelable


fun MessageDto.toMessage(): Message{
    return Message(
        id = id,
        senderId = senderId,
        message = message,
        createdAt = createdAt,
        isUpdate = isUpdate,
        attachmentUrl = attachmentUrl,
        interactions = interactions
    )
}


fun List<String>.getInteractions(): ArrayList<Interaction> {
    val interactions = arrayListOf<Interaction>()
    this.forEach { interactionString ->
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
    if (this.interactions.any { i -> i == interactionId }) {
        this.interactions.toCollection(ArrayList())?.remove(interactionId)
    } else {
        this.interactions.toCollection(ArrayList())?.add(interactionId)
    }
}