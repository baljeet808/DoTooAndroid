package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.TrackerObject
import com.baljeet.youdotoo.presentation.ui.dotoo.DestinationDotooRoute
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsView

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationProjectRoute = "projects"


fun NavGraphBuilder.addProjectViewDestination(
    navController: NavController,
    trackerObject : TrackerObject
){
    composable(
        route = DestinationProjectRoute
    ){
        val viewModel : ProjectsViewModel = hiltViewModel()
        val state by viewModel.projectState
        ProjectsView(
            projectsState = state,
            navigateToDoToos = {project ->
                trackerObject.selectedProjectIndex = trackerObject.projects.indexOf(project)
                navController.navigate(DestinationDotooRoute)
            },
            tracker = trackerObject
        )
    }
}



