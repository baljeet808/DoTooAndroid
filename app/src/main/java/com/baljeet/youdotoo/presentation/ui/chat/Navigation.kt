package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.TrackerObject
import com.baljeet.youdotoo.presentation.ui.chat.components.ChatView

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationMessageRoute = "messages"

fun NavGraphBuilder.addChatViewDestination(
    trackerObject: TrackerObject
){
    composable(
        route = DestinationMessageRoute
    ){
        val viewModel : ChatViewModel = hiltViewModel()
        val messages by viewModel.messagesState
        val tracker = trackerObject.doToos[trackerObject.selectedDoTooIndex]
        viewModel.init(tracker)
        ChatView(
            doToo = tracker,
            messages = messages,
            sendMessage = {messageString ->
                viewModel.sendMessage(
                    projectId = tracker.project.id,
                    doTooId = tracker.doToo.id,
                    messageString = messageString
                )
            },
            toggleIsDone = {
                viewModel.toggleIsDone(trackerObject.doToos[trackerObject.selectedDoTooIndex])
            }
        )
    }
}