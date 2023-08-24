package com.baljeet.youdotoo.presentation.ui.edittask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor


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

            val project = task?.let {
                viewModel.getProject(it.projectId).collectAsState(initial = null)
            }?.value

            task?.let {
                EditTaskView(
                    task = task,
                    updateTask = { name, desc, priority, dueDate, customDate, selectedProject ->
                        viewModel.updateTask(
                            task = task!!,
                            name = name,
                            description = desc,
                            priority = priority,
                            dueDate = dueDate,
                            customDate = customDate,
                            selectedProject = selectedProject
                        )
                        navController.popBackStack()
                    },
                    project = project,
                    onClose = {
                        navController.popBackStack()
                    }
                )
            }?: kotlin.run {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = getLightThemeColor()
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Loading...",
                        color = getTextColor(),
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        letterSpacing = TextUnit(.1f, TextUnitType.Sp)
                    )
                }
            }
        }?: kotlin.run {
            navController.popBackStack()
        }
    }
}
