package com.baljeet.youdotoo.presentation.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.updateInteraction
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.domain.use_cases.messages.GetAllMessagesByProjectIDAsFlowUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.UUID
import javax.inject.Inject

/**
 * Updated by Baljeet singh on 23rd June, 2023 at 5:15PM.
 * **/
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllMessagesByProjectIDAsFlowUseCase: GetAllMessagesByProjectIDAsFlowUseCase
) : ViewModel() {


    private val projectId : String = checkNotNull(savedStateHandle["projectId"])


    private var chatRef = FirebaseFirestore.getInstance().collection("projects")

    fun getAllMessagesOfThisProject() = getAllMessagesByProjectIDAsFlowUseCase(projectId)


    fun toggleIsDone(doToo: DoTooWithProfiles) {
        val newDoToo = doToo.doToo.copy()
        newDoToo.done = doToo.doToo.done.not()
        newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ")
            .plus(if (newDoToo.done) "completed." else "not completed.")
        chatRef
            .document(doToo.project.id)
            .collection("todos")
            .document(doToo.doToo.id)
            .set(newDoToo)
    }

    fun interactWithMessage(
        projectId: String,
        doTooId: String,
        message: MessageEntity,
        emoticon: String
    ) {
        val newMessage = message.copy()
        newMessage.updateInteraction(emoticon)
        chatRef
            .document(projectId)
            .collection("todos")
            .document(doTooId)
            .collection("messages")
            .document(message.id)
            .set(newMessage)
    }


    fun sendMessage(
        projectId: String,
        doTooId: String,
        messageString: String,
        isUpdate: Boolean = false,
        updateMessage: String = ""
    ) {
        val newMessageID = UUID.randomUUID().toString()
        val newMessage = MessageEntity(
            id = newMessageID,
            message = if (isUpdate) {
                updateMessage
            } else {
                messageString
            },
            senderId = SharedPref.userId!!,
            createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime()
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            isUpdate = isUpdate,
            attachmentUrl = null,
            interactions = "",
            projectId = projectId
        )

        chatRef
            .document(projectId)
            .collection("todos")
            .document(doTooId)
            .collection("messages")
            .document(newMessageID)
            .set(newMessage)
    }

}