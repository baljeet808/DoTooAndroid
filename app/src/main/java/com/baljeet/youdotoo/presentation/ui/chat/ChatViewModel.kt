package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.domain.models.Message
import com.baljeet.youdotoo.domain.models.MessageDto
import com.baljeet.youdotoo.domain.models.toMessage
import com.baljeet.youdotoo.domain.models.updateInteraction
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    private val projectId : String = checkNotNull(savedStateHandle["projectId"])

    var messagesState = mutableStateOf<List<Message>>(listOf())
        private set

    private var chatRef = FirebaseFirestore.getInstance().collection("projects")

    init {
        chatRef.document(projectId)
            .collection("messages")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null && error == null) {
                    val messages = ArrayList<Message>()
                    for (message in snapshot) {
                        messages.add(
                            /*Message(
                                id = message.getString("id") ?: "",
                                senderId = message.getString("senderId") ?: "",
                                message = message.getString("message") ?: "",
                                createdAt = message.getLong("createdAt") ?: 0,
                                isUpdate = message.getBoolean("isUpdate") ?: false,
                                attachmentUrl = message.getString("attachmentUrl") ?: "",
                                interactions = (message.get("interactions") as List<String>)
                            )*/
                            message.toObject(MessageDto::class.java).toMessage()
                        )
                    }
                    messagesState.value = messages.sortedBy { it.createdAt }.reversed()
                }
            }
    }


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
        message: Message,
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
        val newMessage = Message(
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
            interactions = arrayListOf()
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