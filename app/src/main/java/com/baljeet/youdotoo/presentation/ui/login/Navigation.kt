package com.baljeet.youdotoo.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationLoginRoute = "login"

fun NavGraphBuilder.addLoginDestination(
    navController: NavController,
    onSignInAttempt : OnAttemptLoginViaGoogle,
    signInState : SignInState
){
    composable(
        route = DestinationLoginRoute
    ){

        val viewModel : LoginViewModel = hiltViewModel()
        val state by viewModel.state

        if(signInState.userData != null){
            onSignInAttempt.resetLoginState()
            viewModel.updateUser(userData = signInState.userData)
        }
        if(state.userData != null){
            navController.navigate(
                DestinationDashboardRoute,
                navOptions {
                    this.popUpTo(navController.graph.startDestinationId)
                }
            )
            viewModel.resetState()
        }
        SignInView(
            attemptToLogin = {
                onSignInAttempt.attemptLoginViaGoogle()
            },
            state = state,
            navigateToTermOfUse = {

            },
            navigateToPolicy = {

            }
        )
    }
}