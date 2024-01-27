package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.presentation.ui.notifications.DestinationNotificationRoute
import com.baljeet.youdotoo.presentation.ui.projectsonly.DestinationProjectOnlyRoute
import com.baljeet.youdotoo.presentation.ui.settings.DestinationSettingsRoute

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationDashboardRoute = "Dashboard"


fun NavGraphBuilder.addDashboardViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationDashboardRoute
    ){
        val viewModel : DashboardViewModel  = hiltViewModel()


        DashboardView(
            userData =  UserData(
                userId = SharedPref.userId ?: "",
                userName = SharedPref.userName,
                userEmail = SharedPref.userEmail,
                profilePictureUrl = SharedPref.userAvatar
            ),
            onClickNotifications = {
                navController.navigate(DestinationNotificationRoute)
            },
            logout = {
                viewModel.signOut()
            },
            onClickSettings = {
                navController.navigate(DestinationSettingsRoute)
            },
            openProfile = {
                navController.navigate("profile_quick_view/".plus(SharedPref.userId))
            },
            navigateToProjectsOnlyView ={
                navController.navigate(
                    DestinationProjectOnlyRoute
                )
            }
        )
    }
}