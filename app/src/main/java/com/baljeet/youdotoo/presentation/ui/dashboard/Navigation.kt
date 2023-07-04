package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.presentation.ui.dashboard.component.DashboardView

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationDashboardRoute = "Dashboard"


fun NavGraphBuilder.addDashboardViewDestination(){
    composable(
        route = DestinationDashboardRoute
    ){
        DashboardView()
    }
}