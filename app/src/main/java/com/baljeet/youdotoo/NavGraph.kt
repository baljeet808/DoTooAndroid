package com.baljeet.youdotoo

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle
import com.baljeet.youdotoo.common.SharedPref
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

    val viewModel : MainViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = if(SharedPref.isUserLoggedIn){
            DestinationDashboardRoute
        }else{
            DestinationLoginRoute
        }
    ){
        addLoginDestination(navController, onSignInAttempt, signInState)
        addSignupDestination(navController)
        addDashboardViewDestination()
    }
}