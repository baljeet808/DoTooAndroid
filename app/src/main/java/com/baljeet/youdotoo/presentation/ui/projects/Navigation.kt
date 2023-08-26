package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.doesUserHavePermissionToEdit
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.presentation.ui.createproject.DestinationCreateProjectRoute


const val DestinationProjectsRoute = "projects"

fun NavGraphBuilder.addProjectsViewDestination(
    navController: NavController
){
    composable(
        route = DestinationProjectsRoute
    ){

        val viewModel : ProjectsViewModel = hiltViewModel()


        val projects by viewModel.projectsWithTaskCount().collectAsState(initial = arrayListOf())
        val pendingTasks by viewModel.pendingTasks().collectAsState(initial = arrayListOf())
        val yesterdayTasks by viewModel.yesterdayTasks().collectAsState(initial = arrayListOf())
        val todayTasks by viewModel.todayTasks().collectAsState(initial = arrayListOf())
        val tomorrowTasks by viewModel.tomorrowTasks().collectAsState(initial = arrayListOf())
        val allOtherTasks by viewModel.allOtherTasks().collectAsState(initial = arrayListOf())
        ProjectsView(
            projects = projects,
            pendingTasks = pendingTasks.map { it.toDoTooItem() },
            yesterdayTasks = yesterdayTasks.map { it.toDoTooItem() },
            todayTasks = todayTasks.map { it.toDoTooItem() },
            tomorrowTasks = tomorrowTasks.map { it.toDoTooItem() },
            allOtherTasks = allOtherTasks.map { it.toDoTooItem() },
            navigateToDoToos = { project ->
                navController.navigate("project/".plus(project.id))
            },
            onToggleTask = { task ->
                val newTask = task.copy()
                newTask.done = task.done.not()
                newTask.updatedBy = SharedPref.userName.plus(" marked this task ")
                    .plus(if (newTask.done) "completed." else "not completed.")
                viewModel.updateTask(newTask)
            },
            navigateToTask = {
                navController.navigate("editTask/".plus(it.id))
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
            deleteTask = { task ->
                viewModel.deleteTask(task)
            },
            updateTaskTitle = { task , title ->
                val newTask = task.copy(
                    title = title
                )
                newTask.updatedBy = SharedPref.userName.plus(" has updated task title.")
                viewModel.updateTask(newTask)
            }
        )
    }
}



