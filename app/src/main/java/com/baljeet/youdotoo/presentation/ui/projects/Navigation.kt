package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.doesUserHavePermissionToEdit
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.presentation.ui.createproject.DestinationCreateProjectRoute


const val DestinationProjectsRoute = "projects"

fun NavGraphBuilder.addProjectsViewDestination(
    navController: NavController
){
    composable(
        route = DestinationProjectsRoute
    ){

        val viewModel : ProjectsViewModel = hiltViewModel()
        val projects by viewModel.projectsWithTaskCount().collectAsStateWithLifecycle(initialValue = arrayListOf())

        ProjectsView(
            projects = projects,
            navigateToDoToos = { project ->
                navController.navigate("project/".plus(project.id))
            },
            onToggleTask = { task ->
                val newTask = task.copy()
                newTask.task.done = task.task.done.not()
                newTask.task.updatedBy = SharedPref.userName.plus(" marked this task ")
                    .plus(if (newTask.task.done) "completed." else "not completed.")
                viewModel.updateTask(newTask.task)
            },
            navigateToTask = {
                navController.navigate("editTask/".plus(it.task.id))
            },
            navigateToCreateTask = {
                val userProjects = projects.filter { project -> doesUserHavePermissionToEdit(project.project) }
                if(userProjects.isNotEmpty()){
                    userProjects.first().let {project ->
                        navController.navigate(
                            "create_task/".plus(project.project.id)
                        )
                    }
                }else{
                    val newProjectId = getRandomId()
                    viewModel.createDummyProject(newProjectId)

                    navController.navigate(
                        "create_task/".plus(newProjectId)
                    )
                }
            },
            navigateToCreateProject = {
                navController.navigate(DestinationCreateProjectRoute)
            },
            updateTaskTitle = { task , title ->
                val newTask = task.task.copy(
                    title = title
                )
                newTask.updatedBy = SharedPref.userName.plus(" has updated task title.")
                viewModel.updateTask(newTask)
            },
            deleteTask = { task ->
                viewModel.deleteTask(task.task)
            }
        )
    }
}



