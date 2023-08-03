package com.baljeet.youdotoo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baljeet.youdotoo.common.OnAttemptLoginViaGoogle
import com.baljeet.youdotoo.presentation.ui.login.DestinationLoginRoute
import com.baljeet.youdotoo.presentation.ui.login.addLoginDestination
import com.baljeet.youdotoo.presentation.ui.signup.addSignupDestination

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun NavGraph(
    navController: NavHostController,
    onSignInAttempt : OnAttemptLoginViaGoogle
) {

    NavHost(
        navController = navController,
        startDestination = DestinationLoginRoute
    ){
        addLoginDestination(navController, onSignInAttempt)
        addSignupDestination(navController)
    }
}