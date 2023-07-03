package com.baljeet.youdotoo

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute
import com.baljeet.youdotoo.presentation.ui.dashboard.addDashboardViewDestination
import com.baljeet.youdotoo.presentation.ui.login.DestinationLoginRoute
import com.baljeet.youdotoo.presentation.ui.login.SignInState
import com.baljeet.youdotoo.presentation.ui.login.addLoginDestination
import com.baljeet.youdotoo.presentation.ui.signup.addSignupDestination

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun NavGraph(
    navController: NavHostController,
    onSignInAttempt : OnAttemptLoginViaGoogle,
    signInState : SignInState
) {
    val viewModel : MainViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = if(true/*SharedPref.isUserLoggedIn*/){
            DestinationDashboardRoute
        }else{
            DestinationLoginRoute
        }
    ){
        addLoginDestination(navController, onSignInAttempt, signInState)
        addSignupDestination(navController)
        addDashboardViewDestination(trackerObject = viewModel.trackerObject)
    }
}