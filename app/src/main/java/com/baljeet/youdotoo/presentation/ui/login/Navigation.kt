package com.baljeet.youdotoo.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.presentation.ui.login.components.SignInView
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectRoute
import com.baljeet.youdotoo.presentation.ui.signup.DestinationSignupRoute

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationLoginRoute = "login"

fun NavGraphBuilder.addLoginDestination(
    navController: NavController
){
    composable(
        route = DestinationLoginRoute
    ){
        val viewModel : LoginViewModel = hiltViewModel()
        val state by viewModel.state

        if(state.userId != null){
            navController.navigate(DestinationProjectRoute)
        }
        SignInView(
            navigateToProjects = {
                navController.navigate(DestinationProjectRoute)
            },
            navigateToSignUp = {
                navController.navigate(DestinationSignupRoute)
            },
            attemptToLogin = { email , password ->
                viewModel.performLogin(email, password)
            },
            state = state,
            navigateToTermOfUse = {

            },
            navigateToPolicy = {

            }
        )
    }
}