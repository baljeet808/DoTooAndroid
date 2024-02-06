package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val DestinationCreateProjectRoute = "create_project"

fun NavGraphBuilder.addCreateProjectViewDestination(
    navController: NavController
){
    composable(
        route = DestinationCreateProjectRoute
    ){

        val viewModel : CreateProjectViewModel = hiltViewModel()

        CreateProjectView(
            createProject = { name: String, description: String, color : String ->
                viewModel.createProject(
                    projectName = name,
                    description = description,
                    projectColor = color
                )
                navController.popBackStack()
            },
            navigateBack = {
                //navigate back
                navController.popBackStack()
            }
        )

    }
}