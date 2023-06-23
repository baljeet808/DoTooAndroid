package com.baljeet.youdotoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.baljeet.youdotoo.ui.NavGraph
import com.baljeet.youdotoo.ui.destinations.*
import com.baljeet.youdotoo.presentation.ui.theme.YouDoTooTheme
import com.baljeet.youdotoo.common.SharedPref
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.DestinationsNavHost


class MainActivity : ComponentActivity() {

    lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application = application)
        )[MainViewModel::class.java]


        setContent {
            SharedPref.init(applicationContext)
            YouDoTooTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navGraph = NavGraph(
                        route = "root",
                        startRoute = if(isUserSignedIn()) ProjectsViewDestination else LoginViewDestination,
                        destinations = listOf(
                            createDoTooViewDestination,
                            createProjectViewDestination,
                            DoTooViewDestination,
                            LoginViewDestination,
                            ProjectsViewDestination,
                            SignupViewDestination,
                            ChatViewDestination
                        )
                    )

                    DestinationsNavHost(
                        navGraph = navGraph
                    )
                }
            }
        }
    }

    private fun isUserSignedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun onStart() {
        super.onStart()
        installSplashScreen()
    }

}

