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
import com.baljeet.youdotoo.presentation.ui.notifications.addNotificationViewDestination
import com.baljeet.youdotoo.presentation.ui.projectinvitation.addProjectInvitationDestination
import com.baljeet.youdotoo.presentation.ui.theme.YouDoTooTheme
import com.baljeet.youdotoo.presentation.ui.themechooser.addThemeChooserViewDestination
import com.baljeet.youdotoo.services.AllBackgroundSnaps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashBoard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(applicationContext)
        setContent {

            val viewModel: DashboardViewModel  = hiltViewModel()

            val serviceIntent = Intent(applicationContext, AllBackgroundSnaps::class.java)

            if (!SharedPref.isSyncOn) {
                serviceIntent.action = AllBackgroundSnaps.ServiceActions.START.toString()
                startService(serviceIntent)
            }

            val firebaseAuth = FirebaseAuth.getInstance()
            val authStateListener = AuthStateListener { auth ->
                if (auth.currentUser == null) {
                    serviceIntent.action = AllBackgroundSnaps.ServiceActions.STOP.toString()
                    stopService(serviceIntent)
                    moveToOnBoarding()
                    viewModel.clearEverything()
                }
            }
            firebaseAuth.addAuthStateListener(authStateListener)

            YouDoTooTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = DestinationDashboardRoute
                ) {
                    addDashboardViewDestination(
                        navController = navController,
                        logout = {
                            firebaseAuth.signOut()
                        }
                    )
                    addNotificationViewDestination(navController)
                    addProjectInvitationDestination(navController)
                    addThemeChooserViewDestination(navController)
                }
            }
        }
    }

    private fun moveToOnBoarding() {
        val intent = Intent(this@DashBoard, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}