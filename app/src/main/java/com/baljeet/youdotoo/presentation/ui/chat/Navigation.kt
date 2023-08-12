package com.baljeet.youdotoo.presentation.ui.chat

import android.webkit.MimeTypeMap
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.dto.AttachmentDto
import com.baljeet.youdotoo.data.mappers.toProject

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationMessageRoute = "messages/{projectId}"

fun NavGraphBuilder.addChatViewDestination(){
    composable(
        route = DestinationMessageRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){

        val viewModel : ChatViewModel = hiltViewModel()

        val project by viewModel.getProjectById().collectAsState(initial = null)
        val participants = project?.let {
            viewModel.getUserProfiles(it.toProject().getUserIds()).collectAsState(initial = listOf())
        }?.value

        val messages = participants?.let {
            viewModel.getAllMessagesOfThisProject().collectAsState(initial = listOf())
        }?.value?: listOf()

        val contentResolver = LocalContext.current.contentResolver

        ChatView(
            participants = participants?:listOf(),
            messages = messages.reversed(),
            sendMessage = { messageString, attachments ->

                val attachmentsDTOs = arrayListOf<AttachmentDto>()
                attachments.forEach { uri ->

                    val mime = MimeTypeMap.getSingleton()

                    mime.getExtensionFromMimeType(contentResolver.getType(uri))?.let {mimeType ->
                        attachmentsDTOs.add(
                            AttachmentDto(
                                uri = uri,
                                mimeType = mimeType
                            )
                        )
                    }
                }

                viewModel.sendMessage(
                    messageString = messageString,
                    isUpdate = false,
                    updateMessage = "",
                    attachments = attachmentsDTOs
                )
            },
            showAttachments = {},
            interactOnMessage = { message, emoticon ->
                viewModel.interactWithMessage(
                    message= message,
                    emoticon = emoticon
                )
            }
        )
    }
}