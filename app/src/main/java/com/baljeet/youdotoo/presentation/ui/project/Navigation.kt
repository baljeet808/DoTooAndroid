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
) {

    composable(
        route = DestinationProjectRoute,
        arguments = listOf(
            navArgument("projectId") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val projectId = backStackEntry.arguments?.getString("projectId")

        val viewModel: ProjectViewModel = hiltViewModel()
        val projectEntity by viewModel.getProjectById().collectAsState(initial = null)
        val tasks by viewModel.getProjectTasks().collectAsState(initial = listOf())


        //compose view should not wait for users to be loaded first
        val users = projectEntity?.let {
            viewModel.getUserProfiles(it.toProject().getUserIds())
                .collectAsState(initial = listOf())
        }?.value ?: listOf()
        
        ProjectView(
            project = projectEntity,
            onToggle = { task ->
                val updatedTask = task.task.copy()
                updatedTask.done = task.task.done.not()
                updatedTask.updatedBy = SharedPref.userName.plus(" marked this task ")
                    .plus(if (updatedTask.done) "completed." else "not completed.")
                viewModel.upsertTask(updatedTask, task.projectEntity.toProject())
            },
            tasks = tasks,
            navigateToCreateTask = {
                navController.navigate(
                    "create_task/".plus(projectId)
                )
            },
            users = users,
            deleteTask = { task ->
                projectEntity?.let { viewModel.deleteTask(task.task, it.toProject()) }
            },
            deleteProject = {
                projectEntity?.toProject()?.let {
                    navController.popBackStack()
                    viewModel.deleteProject(it)
                }
            },
            upsertProject = { updatedProject ->
                viewModel.updateProject(updatedProject)
            },
            onClickInvite = {
                navController.navigate("invitations/".plus(projectId))
            },
            navigateToEditTask = {
                navController.navigate("editTask/".plus(it.task.id))
            },
            navigateToChat = {
                navController.navigate(
                    "messages/${projectId}"
                )
            },
            updateTaskTitle = { task, title ->
                projectEntity?.toProject()?.let {
                    viewModel.upsertTask(
                        task = task.task.copy(
                            title = title,
                            updatedBy = SharedPref.userName.plus(" has updated this task.")
                        ),
                        project = it
                    )
                }
            }
        )

    }


}



