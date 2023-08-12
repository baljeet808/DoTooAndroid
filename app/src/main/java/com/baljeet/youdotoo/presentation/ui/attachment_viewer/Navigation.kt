package com.baljeet.youdotoo.presentation.ui.attachment_viewer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationAttachmentViewerRoute = "attachment_viewer/{attachmentUrl}"

fun NavGraphBuilder.addAttachmentViewerDestination(navController: NavController){
    composable(
        route = DestinationAttachmentViewerRoute,
        arguments = listOf(
            navArgument("attachmentUrl"){
                type = NavType.StringType
            }
        )
    ){ backStackEntry ->
        val attachmentUrl = backStackEntry.arguments?.getString("attachmentUrl")


        AttachmentViewer(
            attachmentUrl = attachmentUrl,
            goBack = { navController.popBackStack() },
            downloadImage = {

            }
        )


    }
}