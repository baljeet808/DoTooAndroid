package com.baljeet.youdotoo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute
import com.baljeet.youdotoo.presentation.ui.dashboard.addDashboardViewDestination
import com.baljeet.youdotoo.presentation.ui.invitation.projectinvitation.addProjectInvitationDestination
import com.baljeet.youdotoo.presentation.ui.login.DestinationLoginRoute
import com.baljeet.youdotoo.presentation.ui.login.addLoginDestination
import com.baljeet.youdotoo.presentation.ui.notifications.addNotificationViewDestination
import com.baljeet.youdotoo.presentation.ui.signup.addSignupDestination
import com.baljeet.youdotoo.services.AllBackgroundSnaps

class DashBoard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

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


    override fun onStart() {
        super.onStart()
        if(!SharedPref.isSyncOn) {
            Intent(applicationContext, AllBackgroundSnaps::class.java).also {
                it.action = AllBackgroundSnaps.ServiceActions.START.toString()
                startService(it)
            }
        }
    }
}