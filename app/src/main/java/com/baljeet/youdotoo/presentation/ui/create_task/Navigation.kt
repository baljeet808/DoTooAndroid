package com.baljeet.youdotoo.presentation.ui.create_task

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DestinationCreateTaskRoute = "create_task/{projectId}"


fun NavGraphBuilder.addCreateTaskViewDestination(
    navController: NavController
){

    composable(
        route = DestinationCreateTaskRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){backStackEntry ->

        val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel : CreateTaskViewModel = hiltViewModel()
        val state by viewModel.createState

        CreateTaskView(
            createState = state
        )

    }


}
