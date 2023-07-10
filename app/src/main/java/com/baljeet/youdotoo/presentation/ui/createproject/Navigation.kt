package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.presentation.ui.createproject.components.CreateProjectView


const val DestinationCreateProjectRoute = "create_project"


fun NavGraphBuilder.addCreateProjectViewDestination(
    navController: NavController
){


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
            },
            navigateBack = {
                //navigate back
                navController.popBackStack()
                viewModel.resetState()
            }
        )

    }


}
