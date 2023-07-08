package com.baljeet.youdotoo.presentation.ui.project

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DestinationProjectRoute = "project/{projectId}"


fun NavGraphBuilder.addProjectViewDestination(
    navController: NavController
){

    composable(
        route = DestinationProjectRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){backStackEntry ->

        val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel : ProjectViewModel = hiltViewModel()
        val state by viewModel.projectState
        ProjectView(
            project = state,
            onToggle={doTooItem, project ->
                viewModel.upsertDoToo(
                    doTooItem, project
                )
            },
            navigateToCreateTask = {projectOwner->
                navController.navigate(
                    "create_task/".plus(projectId).plus("/${projectOwner}")
                )
            }
        )
    }


}



