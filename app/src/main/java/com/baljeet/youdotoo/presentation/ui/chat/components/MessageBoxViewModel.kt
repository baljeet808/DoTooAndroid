package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.dto.AttachmentDto
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.models.Project
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MessageBoxViewModel  @Inject constructor() : ViewModel() {

    private var storageRef = FirebaseStorage.getInstance().reference.child("attachments")


    private var chatRef = FirebaseFirestore.getInstance().collection("projects")

    sealed class AttachmentStates{
        object Empty : AttachmentStates()
        object Sending : AttachmentStates()
        object Sent : AttachmentStates()
        data class CompletedWithErrors(val failedAttachments : ArrayList<AttachmentDto>) : AttachmentStates()
    }

    private var _attachmentStates = MutableStateFlow<AttachmentStates>(AttachmentStates.Empty)
        var attachmentStates = _attachmentStates.asStateFlow()
    fun sendMessage(
        messageString: String,
        isUpdate: Boolean = false,
        updateMessage: String = "",
        attachments: List<AttachmentDto>,
        project : Project
    ) {


        if(updateMessage.isNotEmpty() || messageString.isNotEmpty()) {
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
                projectId = project.id
            )
            chatRef
                .document(project.id)
                .collection("messages")
                .document(newMessageID)
                .set(newMessage)
        }

        if(attachments.isNotEmpty()){
            _attachmentStates.value = AttachmentStates.Sending
        }

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
                            projectId = project.id
                        )

                        chatRef
                            .document(project.id)
                            .collection("messages")
                            .document(newAttachmentMessageId)
                            .set(newAttachmentMessage)
                        if(attachments.last() == attachment){
                            if(attachments.any { it.uploadingFailed }){
                                _attachmentStates.value = AttachmentStates.CompletedWithErrors(attachments.filter { a -> a.uploadingFailed }.toCollection(ArrayList()))
                            }else{
                                _attachmentStates.value = AttachmentStates.Sent
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    attachment.uploadingFailed = true
                    if(attachments.last() == attachment){
                        _attachmentStates.value = AttachmentStates.CompletedWithErrors(attachments.filter { a -> a.uploadingFailed }.toCollection(ArrayList()))
                    }
                }
        }
    }

    fun resetState(){
        _attachmentStates.value = AttachmentStates.Empty
    }

}