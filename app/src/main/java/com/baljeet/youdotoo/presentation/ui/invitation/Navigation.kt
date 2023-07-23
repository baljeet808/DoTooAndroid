package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DestinationInvitationRoute = "invitations/{projectId}"

fun NavGraphBuilder.addInvitationViewDestination(
    navController: NavController
){
    composable(
        route = DestinationInvitationRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){ //backStackEntry ->

       // val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel : InvitationsViewModel  = hiltViewModel()
        val invitations by viewModel.getAllInvitationsByProjectId().collectAsState(initial = listOf())

        InvitationsView(
            invitations = invitations,
            onBackPressed = {
                navController.popBackStack()
            }
        )

    }
}