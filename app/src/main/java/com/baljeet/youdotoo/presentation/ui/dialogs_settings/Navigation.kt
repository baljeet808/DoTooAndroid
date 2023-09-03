package com.baljeet.youdotoo.presentation.ui.dialogs_settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DestinationAppPreferencesRoute = "app_preferences"

fun NavGraphBuilder.addAppPreferencesViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationAppPreferencesRoute
    ) {

        AppPreferencesView(
            goBack = {
                navController.popBackStack()
            }
        )

    }
}