package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.getUserIds
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
        val messages by viewModel.getAllMessagesOfThisProject().collectAsState(initial = listOf())
        val project by viewModel.getProjectById().collectAsState(initial = null)
        val participants = project?.let {
            viewModel.getUserProfiles(it.toProject().getUserIds()).collectAsState(initial = listOf())
        }?.value?: listOf()

        ChatView(
            participants = participants,
            messages = messages,
            sendMessage = { messageString ->
                viewModel.sendMessage(
                    messageString = messageString,
                    isUpdate = false,
                    updateMessage = ""
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