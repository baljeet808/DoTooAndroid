package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationMessageRoute = "messages"

fun NavGraphBuilder.addChatViewDestination(){
    composable(
        route = DestinationMessageRoute
    ){
        val viewModel : ChatViewModel = hiltViewModel()
        val messages by viewModel.messagesState
      //  viewModel.init()
     /*   ChatView(
            doToo = ,
            messages = messages,
            sendMessage = {messageString ->
                viewModel.sendMessage(
                    projectId = ,
                    doTooId = ,
                    messageString = messageString
                )
            },
            toggleIsDone = {
                viewModel.toggleIsDone()
            },
            showAttachments = {},
            interactOnMessage = { message, emoticon ->
                viewModel.interactWithMessage(
                    projectId = ,
                    doTooId =  ,
                    message = message,
                    emoticon = emoticon
                )
            }
        )*/
    }
}