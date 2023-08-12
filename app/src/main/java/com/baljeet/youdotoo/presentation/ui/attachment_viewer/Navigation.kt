package com.baljeet.youdotoo.presentation.ui.attachment_viewer

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DestinationAttachmentViewerRoute = "attachment_viewer/{messageId}"

fun NavGraphBuilder.addAttachmentViewerDestination(navController: NavController) {
    composable(
        route = DestinationAttachmentViewerRoute,
        arguments = listOf(
            navArgument("messageId") {
                type = NavType.StringType
            }
        )
    ) {

        val viewModel: AttachmentViewerViewModel = hiltViewModel()

        val message by viewModel.getMessageAsFlow().collectAsState(initial = null)

        AttachmentViewer(
            message = message,
            goBack = {
                navController.popBackStack()
            },
            downloadImage = {

            }
        )


    }
}