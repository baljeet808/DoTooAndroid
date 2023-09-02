package com.baljeet.youdotoo.presentation.ui.settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.presentation.ui.accounts.DestinationAccountsRoute
import com.baljeet.youdotoo.presentation.ui.themechooser.DestinationThemeChooserRoute

const val DestinationSettingsRoute = "settings"

fun NavGraphBuilder.addSettingsViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationSettingsRoute
    ) {

        SettingsView(
            onClose = {
                navController.popBackStack()
            },
            onClickThemes = {
                navController.navigate(DestinationThemeChooserRoute)
            },
            onClickDashboard = {

            },
            onClickDialogPref = {

            },
            onClickAccount = {
                navController.navigate(DestinationAccountsRoute)
            }
        )

    }
}