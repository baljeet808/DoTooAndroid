package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationDeclined
import com.baljeet.youdotoo.common.InvitationPending

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
        val users by viewModel.getAllUsers().collectAsState(initial = listOf())
        val project by viewModel.getProject().collectAsState(initial = null)

        InvitationsView(
            invitations = invitations,
            users = users,
            onBackPressed = {
                navController.popBackStack()
            },
            onUpdateAccess = { userInvitation, accessType ->
                viewModel.updateUserAccessType(userInvitation,accessType)
            },
            onClickActionButton = { userInvitation ->
                if(userInvitation.invitationEntity == null){
                    userInvitation.user?.let {user ->
                        viewModel.sendInvite(
                            email = user.email,
                            accessType = 2
                        )
                    }
                }else if(userInvitation.invitationEntity?.status == InvitationPending){
                    viewModel.archiveInvitation(
                        userInvitation.invitationEntity!!
                    )
                } else if (userInvitation.invitationEntity?.status == InvitationAccepted){
                    viewModel.removeUserFromProject(userInvitation)
                } else if(userInvitation.invitationEntity?.status == InvitationDeclined){
                    viewModel.resendInvitation(
                        userInvitation.invitationEntity!!
                    )
                }
            },
            sendInvite = { email, accessType ->
                viewModel.sendInvite(email, accessType)
            },
            project = project
        )

    }
}