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
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.Project
import kotlinx.datetime.LocalDate


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

        val projects by viewModel.getProjects().collectAsState(initial = listOf())

        UpsertTaskView(
            createTask = { title: String, description: String, priority: Priorities, dueDate: DueDates, customDate: LocalDate?, selectedProject : Project ->
                viewModel.createTask(
                    name = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    customDate = customDate,
                    selectedProject = selectedProject
                )
                navController.popBackStack()
            },
            navigateBack = {
                //navigate back
                navController.popBackStack()
            },
            projectId = projectId?:"",
            projects = projects.filter { project ->  (project.ownerId == SharedPref.userId) || (project.collaboratorIds.contains(SharedPref.userId!!)) }
        )

    }


}
