package com.baljeet.youdotoo.presentation.ui.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationLoginRoute = "login"

fun NavGraphBuilder.addLoginDestination(
    navController: NavController,
    onSignInAttempt : OnAttemptLoginViaGoogle
){
    composable(
        route = DestinationLoginRoute
    ){
        SignInView(
            attemptToLogin = {
                onSignInAttempt.attemptLoginViaGoogle()
            },
            navigateToTermOfUse = {

            },
            navigateToPolicy = {

            }
        )
    }
}