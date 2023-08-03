package com.baljeet.youdotoo

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute
import com.baljeet.youdotoo.presentation.ui.dashboard.addDashboardViewDestination
import com.baljeet.youdotoo.presentation.ui.invitation.projectinvitation.addProjectInvitationDestination
import com.baljeet.youdotoo.presentation.ui.notifications.addNotificationViewDestination
import com.baljeet.youdotoo.presentation.ui.theme.YouDoTooTheme
import com.baljeet.youdotoo.services.AllBackgroundSnaps
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashBoard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(applicationContext)
        setContent {

            val viewModel : DashboardViewModel = hiltViewModel()

            viewModel.engageFun()

            YouDoTooTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = DestinationDashboardRoute
                ){
                    addDashboardViewDestination(navController)
                    addNotificationViewDestination(navController)
                    addProjectInvitationDestination(navController)
                }
            }
        }
    }


    override fun onStart() {
        super.onStart()
        SharedPref.init(applicationContext)
        if(!SharedPref.isSyncOn) {
            Intent(applicationContext, AllBackgroundSnaps::class.java).also {
                it.action = AllBackgroundSnaps.ServiceActions.START.toString()
                startService(it)
            }
        }
    }
}