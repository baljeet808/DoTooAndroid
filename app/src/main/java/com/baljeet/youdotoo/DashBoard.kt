package com.baljeet.youdotoo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.ConstSampleAvatarUrl
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute
import com.baljeet.youdotoo.presentation.ui.dashboard.addDashboardViewDestination
import com.baljeet.youdotoo.presentation.ui.notifications.addNotificationViewDestination
import com.baljeet.youdotoo.presentation.ui.profile_quick_view.addProfileQuickViewDestination
import com.baljeet.youdotoo.presentation.ui.projectinvitation.addProjectInvitationDestination
import com.baljeet.youdotoo.presentation.ui.settings.addSettingsViewDestination
import com.baljeet.youdotoo.presentation.ui.theme.YouDoTooTheme
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.themechooser.addThemeChooserViewDestination
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashBoard : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPref.init(applicationContext)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = getLightThemeColor()
                    )
            ) {

                val viewModel: DashboardViewModel = hiltViewModel()


                val firebaseAuth = FirebaseAuth.getInstance()
                val authStateListener = AuthStateListener { auth ->
                    if (auth.currentUser == null) {
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
                        addProfileQuickViewDestination(navController)
                        addSettingsViewDestination(navController)
                    }
                }
            }
        }

        val firebaseMessaging = FirebaseMessaging.getInstance()
        firebaseMessaging.subscribeToTopic("dotoo_new_project_notifications")

        if(SharedPref.firebaseToken.isBlank()) {
            firebaseMessaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Log for - ", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                if (token.isNotBlank()) {
                    SharedPref.firebaseToken = token
                    val db = FirebaseFirestore.getInstance()

                    Firebase.auth.currentUser?.let {
                        val newUser = User(
                            id = it.uid,
                            name = it.displayName ?: "Unknown",
                            email = it.email ?: "Not given",
                            joined = SharedPref.userJoined,
                            avatarUrl = it.photoUrl?.toString() ?: ConstSampleAvatarUrl,
                            firebaseToken = token
                        )

                        db.collection("users")
                            .document(newUser.id)
                            .set(newUser)
                            .addOnSuccessListener {
                                Log.d("Log for - ", "New token assigned to user.")
                            }.addOnFailureListener {
                                Log.d(
                                    "Log for - ",
                                    "Failed to upload to new token to user profile."
                                )
                            }
                    } ?: kotlin.run {
                        Log.d(
                            "Log for - ",
                            "Failed to upload to new token to user profile. null auth"
                        )
                    }
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