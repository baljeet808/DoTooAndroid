package com.baljeet.youdotoo.presentation.ui.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DestinationSettingsRoute = "settings"

fun NavGraphBuilder.addSettingsViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationSettingsRoute
    ){

    }
}