package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProjectWithEverything
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectView
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsView


const val DestinationProjectsRoute = "projects"
const val DestinationProjectRoute = "project/{projectId}"


fun NavGraphBuilder.addProjectViewDestination(
    navController: NavController
){
    composable(
        route = DestinationProjectsRoute
    ){
        val viewModel : ProjectsViewModel = hiltViewModel()
        val state by viewModel.projectState
        ProjectsView(
            projectsState = state,
            navigateToDoToos = { project ->
                navController.navigate("project/".plus(project.id))
            },
            userId = SharedPref.userId!!,
            isUserAPro = SharedPref.isUserAPro,
            userName = SharedPref.userName.split(" ").first(),
            onToggleTask = {

            },
            navigateToTask = {

            }
        )
    }

    composable(
        route = DestinationProjectRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            }
        )
    ){
        ProjectView(project = getSampleProjectWithEverything())
    }

}



