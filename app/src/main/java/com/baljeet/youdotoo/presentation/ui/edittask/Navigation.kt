package com.baljeet.youdotoo.presentation.ui.edittask

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


const val DestinationEditTaskRoute = "editTask/{taskId}"


fun NavGraphBuilder.addEditTaskViewDestination(
    navController: NavController
) {

    composable(
        route = DestinationEditTaskRoute,
        arguments = listOf(
            navArgument("taskId") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val taskId = backStackEntry.arguments?.getString("taskId")

        taskId?.let {
            val viewModel: EditTaskViewModel = hiltViewModel()
            val task by viewModel.getTaskByIdAsFlow(taskId).collectAsState(initial = null)

            EditTaskView(
                task = task,
                updateTask = { taskToUpdate, title ->
                    viewModel.updateTask(taskToUpdate, title)
                    navController.popBackStack()
                },
                onClose = {
                    navController.popBackStack()
                }
            )
        }?: kotlin.run {
            navController.popBackStack()
        }
    }
}
