package com.baljeet.youdotoo.presentation.ui.dasboard_settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DestinationDashboardSettingsRoute = "dashboard_settings"

fun NavGraphBuilder.addDashboardSettingsViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationDashboardSettingsRoute
    ) {

        DashboardSettingsView(
            goBack = {
                navController.popBackStack()
            }
        )
    }
}