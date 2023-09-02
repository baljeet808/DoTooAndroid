package com.baljeet.youdotoo.presentation.ui.accounts

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

const val DestinationAccountsRoute = "account"

fun NavGraphBuilder.addAccountsViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationAccountsRoute
    ) {

        val viewModel: AccountViewModel = hiltViewModel()

        val user by viewModel.userAsFlow().collectAsState(initial = null)

        AccountView(
            user = user,
            onClose = {
                navController.popBackStack()
            }
        )

    }
}