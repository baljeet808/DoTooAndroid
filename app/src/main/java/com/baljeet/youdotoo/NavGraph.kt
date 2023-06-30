package com.baljeet.youdotoo

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.chat.addChatViewDestination
import com.baljeet.youdotoo.presentation.ui.dotoo.addDotooViewDestination
import com.baljeet.youdotoo.presentation.ui.login.DestinationLoginRoute
import com.baljeet.youdotoo.presentation.ui.login.addLoginDestination
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectRoute
import com.baljeet.youdotoo.presentation.ui.projects.addProjectViewDestination
import com.baljeet.youdotoo.presentation.ui.signup.addSignupDestination

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun NavGraph(navController: NavHostController) {
    val viewModel : MainViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = if(SharedPref.isUserLoggedIn){
            DestinationProjectRoute
        }else{
            DestinationLoginRoute
        }
    ){
        addLoginDestination(navController)
        addSignupDestination(navController)
        addProjectViewDestination(navController, trackerObject = viewModel.trackerObject)
        addDotooViewDestination(navController = navController, trackerObject = viewModel.trackerObject)
        addChatViewDestination(trackerObject = viewModel.trackerObject)
    }
}