package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.presentation.ui.createproject.CreateProjectViewModel
import com.baljeet.youdotoo.presentation.ui.createproject.CreateProjectView


const val DestinationProjectsRoute = "projects"

const val DestinationCreateProjectRoute = "create_project"


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
            userId = SharedPref.userId!!,
            userName = SharedPref.userName.split(" ").first(),
            onToggleTask = {
                viewModel.upsertDoToo(it)
            },
            navigateToTask = {

            },
            navigateToCreateTask = {
                val userProjects = projects.filter { project -> project.project.ownerId == SharedPref.userId!! }
                if(userProjects.isNotEmpty()){
                    userProjects.first().let {project ->
                        navController.navigate(
                            "create_task/".plus(project.project.id).plus("/${true}")
                        )
                    }
                }else{
                    val newProjectId = getRandomId()
                    viewModel.createDummyProject(newProjectId)

                    navController.navigate(
                        "create_task/".plus(newProjectId).plus("/${true}")
                    )
                }
            },
            navigateToCreateProject = {
                navController.navigate(DestinationCreateProjectRoute)
            },
            deleteTask = { task ->
                viewModel.deleteTask(task)
            }
        )
    }
    composable(
        route = DestinationCreateProjectRoute
    ){

        val viewModel : CreateProjectViewModel = hiltViewModel()
        val state by viewModel.createState

        if(state){
            navController.popBackStack()
            viewModel.resetState()
        }
        CreateProjectView(
            createProject = { name: String, description: String, color : Long ->
                viewModel.createProject(
                    projectName = name,
                    description = description,
                    projectColor = color
                )
                navController.popBackStack()
                viewModel.resetState()
            },
            navigateBack = {
                //navigate back
                navController.popBackStack()
                viewModel.resetState()
            }
        )

    }
}



