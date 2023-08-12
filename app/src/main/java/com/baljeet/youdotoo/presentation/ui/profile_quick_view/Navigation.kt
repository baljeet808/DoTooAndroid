package com.baljeet.youdotoo.presentation.ui.profile_quick_view

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val DestinationProfileQuickViewRoute = "profile_quick_view/{userId}"


fun NavGraphBuilder.addProfileQuickViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationProfileQuickViewRoute,
        arguments = listOf(
            navArgument(
                name = "userId"
            ) {
                type = NavType.StringType
            }
        )
    ) {

        val viewModel: ProfileQuickViewViewModel = hiltViewModel()
        val user by viewModel.getUserAsFlow().collectAsState(initial = null)

        ProfileQuickView(
            user = user,
            goBack = {
                navController.popBackStack()
            }
        )
    }
}