package com.baljeet.youdotoo.presentation.ui.notifications

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.presentation.ui.invitation.DestinationProjectInvitationDetailRoute


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
            onNotificationClick = { notification ->
                when(notification.notificationType){
                    EnumNotificationType.INVITATION -> {
                        navController.navigate(DestinationProjectInvitationDetailRoute.plus(notification.invitationId))
                    }
                    EnumNotificationType.PROJECT_UPDATE -> TODO()
                    EnumNotificationType.TASK_UPDATE -> TODO()
                    EnumNotificationType.MESSAGE -> TODO()
                    EnumNotificationType.GENERAL -> TODO()
                }
            },
            onDeleteNotification = { notification ->
                viewModel.deleteNotification(notification)
            },
            onClearAll = {

            },
            onClickSettings ={

            },
            onClose = {
                navController.popBackStack()
            }
        )

    }

}



