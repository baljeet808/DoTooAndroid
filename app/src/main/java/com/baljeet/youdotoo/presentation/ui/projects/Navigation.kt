package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsView


const val DestinationProjectsRoute = "projects"


fun NavGraphBuilder.addProjectsViewDestination(
    navController: NavController
){
    composable(
        route = DestinationProjectsRoute
    ){
        val viewModel : ProjectsViewModel = hiltViewModel()
        val projects by viewModel.projectsWithTaskCount().collectAsState(initial = arrayListOf())
        val todayTasks by viewModel.todayTasks().collectAsState(initial = arrayListOf())
        ProjectsView(
            projects = projects,
            todayTasks = todayTasks.map { it.toDoTooItem() },
            navigateToDoToos = { project ->
                navController.navigate("project/".plus(project.id))
            },
            userId = SharedPref.userId!!,
            userName = SharedPref.userName.split(" ").first(),
            onToggleTask = {
                viewModel.upsertDoToo(it)
            },
            navigateToTask = {

            }
        )
    }
}



