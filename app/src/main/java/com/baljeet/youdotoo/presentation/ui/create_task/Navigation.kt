package com.baljeet.youdotoo.presentation.ui.create_task

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.domain.models.Project
import kotlinx.datetime.LocalDate


const val DestinationCreateTaskRoute = "create_task/{projectId}/{projectOwner}"


fun NavGraphBuilder.addCreateTaskViewDestination(
    navController: NavController
){

    composable(
        route = DestinationCreateTaskRoute,
        arguments = listOf(
            navArgument("projectId"){
                type = NavType.StringType
            },
            navArgument("projectOwner"){
                type = NavType.BoolType
            }
        )
    ){backStackEntry ->

        val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel : CreateTaskViewModel = hiltViewModel()
        val state by viewModel.createState

        val projects by viewModel.getProjects().collectAsState(initial = listOf())

        if(state){
            navController.popBackStack()
            viewModel.resetState()
        }
        CreateTaskView(
            createTask = { title: String, description: String, priority: Priorities, dueDate: DueDates, customDate: LocalDate?, selectedProject : Project ->
                viewModel.createTask(
                    name = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    customDate = customDate,
                    selectedProject = selectedProject
                )
            },
            navigateBack = {
                //navigate back
                navController.popBackStack()
                viewModel.resetState()
            },
            projectId = projectId!!,
            projects = projects
        )

    }


}
