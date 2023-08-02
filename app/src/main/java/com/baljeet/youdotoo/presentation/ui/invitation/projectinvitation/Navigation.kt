package com.baljeet.youdotoo.presentation.ui.invitation.projectinvitation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DestinationProjectInvitationDetailRoute = "projectInvitationDetail/"
fun NavGraphBuilder.addProjectInvitationDestination(navController: NavController) {

    composable(
        route = DestinationProjectInvitationDetailRoute.plus("{invitationId}"),
        arguments = listOf(
            navArgument("invitationId") {
                type = NavType.StringType
            }
        )
    ) {

        val viewModel: ProjectInvitationDetailViewModel = hiltViewModel()

        val invitation by viewModel.getInvitationByIdAsFlow().collectAsState(initial = null)



        ProjectInvitationDetailView(
            invitation = invitation,
            acceptInvitation = {
                viewModel.acceptInvitation(it)
            },
            declineInvitation = {
                viewModel.declineInvitation(it)
            },
            onClose = {
                navController.popBackStack()
            }
        )

    }
}