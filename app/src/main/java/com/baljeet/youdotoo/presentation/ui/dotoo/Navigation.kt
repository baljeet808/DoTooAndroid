package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.presentation.ui.chat.DestinationMessageRoute
import com.baljeet.youdotoo.presentation.ui.createDoToo.CreateDoTooViewModel
import com.baljeet.youdotoo.presentation.ui.createDoToo.components.createDoTooView
import com.baljeet.youdotoo.presentation.ui.dotoo.components.DoTooView
import kotlinx.datetime.LocalDateTime

/**
 * Updated by Baljeet singh.
 * **/
const val DestinationDotooRoute = "Dotoo"
const val DestinationCreateDotooRoute = "CreateDotoo"

fun NavGraphBuilder.addDotooViewDestination(
    navController: NavController
){
    composable(
        route = DestinationDotooRoute
    ){
        val viewModel : DoToosViewModel = hiltViewModel()
        val state by viewModel.doToosState
      //  viewModel.init()
       /* DoTooView(
            navigateToCreateDoToo = {
                navController.navigate(DestinationCreateDotooRoute)
            },
            toggleDoToo = { doToo ->
                viewModel.toggleIsDone(doToo)
            },
            navigateToChatView = {doToo ->
                navController.navigate(DestinationMessageRoute)
            },
            project = ,
            doToosState = state

        )*/
    }

    composable(
        route = DestinationCreateDotooRoute
    ){
        val viewModel : CreateDoTooViewModel = hiltViewModel()
        val state by viewModel.createState
      /*  createDoTooView(
         //   project = ,
            navigateBack = {
                navController.popBackStack()
            },
            createState = state,
            createDoToo = { projectId: String, title: String, description: String, priority: Priorities, dueDate: DueDates, customDate: LocalDateTime? ->
                viewModel.createDoToo(
                    projectId = projectId,
                    name = title,
                    description = description,
                    priority = priority,
                    dueDate = dueDate,
                    customDate = customDate
                )
            }
        )*/
    }
}