package com.baljeet.youdotoo.presentation.ui.project

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.mappers.toProject


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
        val project by viewModel.getProjectById().collectAsState(initial = null)
        val tasks by viewModel.getProjectTasks().collectAsState(initial = listOf())
        val users = project?.let {
             viewModel.getUserProfiles(it.toProject().getUserIds()).collectAsState(initial = listOf())
        }?.value?: listOf()
        ProjectView(
            project = project,
            onToggle={doTooItem, selectedProject ->
                viewModel.upsertDoToo(
                    doTooItem, selectedProject
                )
            },
            tasks = tasks,
            userId = SharedPref.userId!!,
            navigateToCreateTask = {projectOwner->
                navController.navigate(
                    "create_task/".plus(projectId).plus("/${projectOwner}")
                )
            },
            users = users,
            deleteTask = { task ->
                viewModel.deleteTask(task)
            },
            deleteProject = {
                project?.toProject()?.let {
                    viewModel.deleteProject(it)
                    navController.popBackStack()
                }
            },
            upsertProject = { updatedProject ->
                viewModel.upsertProject(updatedProject)
            }
        )
    }


}



