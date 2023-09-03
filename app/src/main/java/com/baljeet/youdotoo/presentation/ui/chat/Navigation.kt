package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toProject

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationMessageRoute = "messages/{projectId}"

fun NavGraphBuilder.addChatViewDestination(navController: NavController){
    composable(
        route = DestinationMessageRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){

        val viewModel : ChatViewModel = hiltViewModel()

        val lazyPagedMessages : LazyPagingItems<MessageEntity> = viewModel.getAllMessagesOfThisProject().collectAsLazyPagingItems()

        val project by viewModel.getProjectById().collectAsState(initial = null)

        val participants : List<UserEntity> = project?.let {
            viewModel.getUsersOFProject(it.toProject()).collectAsState(initial = listOf())
        }?.value?: listOf()



        ChatView(
            participants = participants,
            messages = lazyPagedMessages,
            showAttachment = {
                navController.navigate(  "attachment_viewer/".plus(it.id))
            },
            interactOnMessage = { message, emoticon ->
                viewModel.interactWithMessage(
                    message= message,
                    emoticon = emoticon
                )
            },
            onClose={
                navController.popBackStack()
            },
            project = project?.toProject()
        )
    }
}