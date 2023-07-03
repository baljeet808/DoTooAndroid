package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.TrackerObject
import com.baljeet.youdotoo.presentation.ui.dashboard.component.DashboardView
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectRoute

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationDashboardRoute = "Dashboard"


fun NavGraphBuilder.addDashboardViewDestination(
    navController: NavController,
    trackerObject : TrackerObject
){
    composable(
        route = DestinationDashboardRoute
    ){
        DashboardView(
            navigateToSettings = {

            },
            navigateToAccount = {

            },
            navigateToAllProjects = {
                navController.navigate(DestinationProjectRoute)
            }
        )
    }
}