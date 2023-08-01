package com.baljeet.youdotoo.presentation.ui.notifications

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val DestinationNotificationRoute = "Notification"


fun NavGraphBuilder.addNotificationViewDestination(
    navController: NavController
){

    composable(
        route = DestinationNotificationRoute
    ){


        val viewModel : NotificationsViewModel = hiltViewModel()
        val notifications by viewModel.getAllNotificationsAsFlow().collectAsState(initial = listOf())
        NotificationsView(
            notifications = notifications,
            onNotificationClick = {

            },
            onDeleteNotification = { notification ->
                viewModel.deleteNotification(notification)
            },
            onClearAll = {

            },
            onClickSettings ={

            }
        )

    }

}



