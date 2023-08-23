package com.baljeet.youdotoo.presentation.ui.notifications

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.presentation.ui.projectinvitation.DestinationProjectInvitationDetailRoute


const val DestinationNotificationRoute = "Notification"


fun NavGraphBuilder.addNotificationViewDestination(
    navController: NavController
) {

    composable(
        route = DestinationNotificationRoute
    ) {


        val viewModel: NotificationsViewModel = hiltViewModel()
        val notifications by viewModel.getAllNotificationsAsFlow()
            .collectAsState(initial = listOf())
        NotificationsView(
            notifications = notifications,
            onNotificationClick = { notification ->
                when (notification.notificationType) {
                    EnumNotificationType.NewInvitation -> {
                        navController.navigate(
                            DestinationProjectInvitationDetailRoute.plus(
                                notification.invitationId
                            )
                        )
                    }
                    EnumNotificationType.NewMessage -> TODO()
                    EnumNotificationType.ProjectUpdate -> TODO()
                    EnumNotificationType.TaskUpdate -> TODO()
                    EnumNotificationType.AccessUpdate -> TODO()
                    EnumNotificationType.InvitationUpdate -> TODO()
                    EnumNotificationType.MessageUpdate -> TODO()
                    EnumNotificationType.General -> TODO()
                }
            },
            onDeleteNotification = { notification ->
                viewModel.deleteNotification(notification)
            },
            onClearAll = {
                viewModel.deleteAllNotifications()
            },
            onClose = {
                navController.popBackStack()
            }
        )

    }

}



