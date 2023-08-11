package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

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
    ){ backStackEntry ->

        val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel : ChatViewModel = hiltViewModel()
        val messages by viewModel.getAllMessagesOfThisProject().collectAsState(initial = listOf())

        ChatView(
            messages = messages,
            sendMessage = {messageString ->

            },
            toggleIsDone = {

            },
            showAttachments = {},
            interactOnMessage = { message, emoticon ->

            }
        )
    }
}