package com.baljeet.youdotoo.presentation.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.common.updateInteraction
import com.baljeet.youdotoo.data.dto.AttachmentDto
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.use_cases.messages.GetAllMessagesByProjectIDAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersByIdsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Updated by Baljeet singh on 23rd June, 2023 at 5:15PM.
 * **/
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllMessagesByProjectIDAsFlowUseCase: GetAllMessagesByProjectIDAsFlowUseCase,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase,
    private val getUsersByIdsUseCase: GetUsersByIdsUseCase
) : ViewModel() {


    private var storageRef = FirebaseStorage.getInstance().reference.child("attachments")

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])


    private var chatRef = FirebaseFirestore.getInstance().collection("projects")


    fun getProjectById(): Flow<ProjectEntity> = getProjectByIdAsFlowUseCase(projectId)

    fun getUserProfiles(ids: List<String>): Flow<List<UserEntity>> = getUsersByIdsUseCase(ids)

    fun getAllMessagesOfThisProject() = getAllMessagesByProjectIDAsFlowUseCase(projectId)


    fun interactWithMessage(
        message: MessageEntity,
        emoticon: String
    ) {
        val newMessage = message.copy()
        newMessage.updateInteraction(emoticon)
        chatRef
            .document(projectId)
            .collection("messages")
            .document(message.id)
            .set(newMessage)
    }


    fun sendMessage(
        messageString: String,
        isUpdate: Boolean = false,
        updateMessage: String = "",
        attachments: List<AttachmentDto>
    ) {
        val newMessageID = getRandomId()
        val newMessage = MessageEntity(
            id = newMessageID,
            message = if (isUpdate) {
                updateMessage
            } else {
                messageString
            },
            senderId = SharedPref.userId!!,
            createdAt = getSampleDateInLong(),
            isUpdate = isUpdate,
            attachmentUrl = null,
            attachmentName = null,
            interactions = "",
            projectId = projectId
        )

        chatRef
            .document(projectId)
            .collection("messages")
            .document(newMessageID)
            .set(newMessage)

        attachments.forEach { attachment ->

            val imageName =
                System.currentTimeMillis().toString().plus(".").plus(attachment.mimeType)

            val imageStorageRef = storageRef.child(imageName)

            imageStorageRef.putFile(attachment.uri)
                .addOnSuccessListener {
                    imageStorageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val newAttachmentMessageId = getRandomId()

                        val newAttachmentMessage = MessageEntity(
                            id = newAttachmentMessageId,
                            message = "",
                            senderId = SharedPref.userId!!,
                            createdAt = getSampleDateInLong(),
                            isUpdate = isUpdate,
                            attachmentUrl = downloadUri.toString(),
                            attachmentName = imageName,
                            interactions = "",
                            projectId = projectId
                        )

                        chatRef
                            .document(projectId)
                            .collection("messages")
                            .document(newAttachmentMessageId)
                            .set(newAttachmentMessage)
                    }
                }

        }
    }
}