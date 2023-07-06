package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.dotoo.DestinationDotooRoute
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsView

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationProjectRoute = "projects"


fun NavGraphBuilder.addProjectViewDestination(
    navController: NavController
){
    composable(
        route = DestinationProjectRoute
    ){
        val viewModel : ProjectsViewModel = hiltViewModel()
        val state by viewModel.projectState
        ProjectsView(
            projectsState = state,
            navigateToDoToos = { project ->
                navController.navigate(DestinationDotooRoute)
            },
            userId = SharedPref.userId!!,
            isUserAPro = SharedPref.isUserAPro,
            userName = SharedPref.userName.split(" ").first()
        )
    }
}



