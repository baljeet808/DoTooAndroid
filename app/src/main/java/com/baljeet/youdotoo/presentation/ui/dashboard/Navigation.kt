package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.presentation.ui.notifications.DestinationNotificationRoute
import com.baljeet.youdotoo.presentation.ui.themechooser.DestinationThemeChooserRoute

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationDashboardRoute = "Dashboard"


fun NavGraphBuilder.addDashboardViewDestination(
    navController: NavHostController,
    logout : () -> Unit
) {
    composable(
        route = DestinationDashboardRoute
    ){
        val viewModel : DashboardViewModel  = hiltViewModel()

        val allTasks by viewModel.allTasks().collectAsState(initial = arrayListOf())

        DashboardView(
            allTasks = allTasks,
            userData =  UserData(
                userId = SharedPref.userId ?: "",
                userName = SharedPref.userName,
                userEmail = SharedPref.userEmail,
                profilePictureUrl = SharedPref.userAvatar
            ),
            onClickNotifications = {
                navController.navigate(DestinationNotificationRoute)
            },
            logout = logout,
            onClickSettings = {
                navController.navigate(DestinationThemeChooserRoute)
            }
        )
    }
}