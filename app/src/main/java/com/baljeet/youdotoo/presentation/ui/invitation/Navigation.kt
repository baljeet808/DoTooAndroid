package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val DestinationInvitationRoute = "Dotoo"

fun NavGraphBuilder.addInvitationViewDestination(
    navController: NavController
){
    composable(
        route = DestinationInvitationRoute
    ){

    }
}