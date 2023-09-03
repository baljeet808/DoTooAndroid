package com.baljeet.youdotoo.presentation.ui.projectsonly

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val DestinationProjectOnlyRoute = "projectsOnly"


fun NavGraphBuilder.addProjectOnlyViewDestination(
    navController: NavController
) {

    composable(
        route = DestinationProjectOnlyRoute
    ) {

        val viewModel: ProjectsOnlyViewModel = hiltViewModel()

        val projectsWithTasks by viewModel.getProjectsWithTasks().collectAsState(initial = listOf())

        ProjectsOnlyView(
            projects = projectsWithTasks,
            onClose = {
                navController.popBackStack()
            }
        )
    }
}



