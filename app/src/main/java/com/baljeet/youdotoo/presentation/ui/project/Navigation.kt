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
    ){ backStackEntry ->

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
                val newDoToo = doTooItem.copy()
                newDoToo.done = doTooItem.done.not()
                newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ").plus(if(newDoToo.done)"completed." else "not completed.")
                viewModel.upsertTask(
                    newDoToo, selectedProject
                )
            },
            tasks = tasks,
            navigateToCreateTask = {
                navController.navigate(
                    "create_task/".plus(projectId)
                )
            },
            users = users,
            deleteTask = { task ->
                project?.toProject()?.let {
                    viewModel.deleteTask(task,it)
                }
            },
            deleteProject = {
                project?.toProject()?.let {
                    viewModel.deleteProject(it)
                    navController.popBackStack()
                }
            },
            upsertProject = { updatedProject ->
                viewModel.updateProject(updatedProject)
            },
            onClickInvite = {
                navController.navigate("invitations/".plus(projectId))
            },
            navigateToEditTask = {
                navController.navigate("editTask/".plus(it.id))
            },
            navigateToChat = {
                navController.navigate(
                    "messages/${projectId}"
                )
            },
            updateTaskTitle = { task, title->
                viewModel.upsertTask(
                    task = task.copy(
                        title = title,
                        updatedBy = SharedPref.userName.plus(" has updated this task.")
                    ),
                    project = project!!.toProject()
                )
            }
        )
    }


}



