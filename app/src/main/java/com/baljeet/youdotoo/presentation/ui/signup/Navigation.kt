package com.baljeet.youdotoo.presentation.ui.signup

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.presentation.ui.dashboard.DestinationDashboardRoute
import com.baljeet.youdotoo.presentation.ui.signup.components.SignupView

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationSignupRoute = "signup"

fun NavGraphBuilder.addSignupDestination(
    navController: NavController
){
    composable(
        route = DestinationSignupRoute
    ){
        val viewModel : SignupViewModel = hiltViewModel()
        val state by viewModel.state
        SignupView(
            navigateToProjects = {
                navController.navigate(DestinationDashboardRoute)
            },
            signupState = state,
            performSignup = { email, password, name ->
                viewModel.performSignup(name, email, password)
            }
        )
    }
}