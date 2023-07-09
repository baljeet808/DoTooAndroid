package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Updated by Baljeet singh.
 * **/
const val DestinationDotooRoute = "Dotoo"

fun NavGraphBuilder.addDotooViewDestination(
    navController: NavController
){
    composable(
        route = DestinationDotooRoute
    ){
        val viewModel : DoToosViewModel = hiltViewModel()
        val state by viewModel.doToosState
      //  viewModel.init()
       /* DoTooView(
            navigateToCreateDoToo = {
                navController.navigate(DestinationCreateDotooRoute)
            },
            toggleDoToo = { doToo ->
                viewModel.toggleIsDone(doToo)
            },
            navigateToChatView = {doToo ->
                navController.navigate(DestinationMessageRoute)
            },
            project = ,
            doToosState = state

        )*/
    }
}