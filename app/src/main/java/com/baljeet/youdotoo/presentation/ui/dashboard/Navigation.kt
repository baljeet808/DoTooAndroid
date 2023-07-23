package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationDashboardRoute = "Dashboard"


fun NavGraphBuilder.addDashboardViewDestination(){
    composable(
        route = DestinationDashboardRoute
    ){
        val viewModel : DashboardViewModel  = hiltViewModel()

        val allTasks by viewModel.allTasks().collectAsState(initial = arrayListOf())

        DashboardView(
            allTasks = allTasks
        )
    }
}