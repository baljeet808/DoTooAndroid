package com.baljeet.youdotoo.presentation.ui.themechooser

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationThemeChooserRoute = "ThemeChooser"


fun NavGraphBuilder.addThemeChooserViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationThemeChooserRoute
    ){
        val viewModel : ColorPalettesViewModel = hiltViewModel()

        val allPalette by viewModel.getColorPalettes().collectAsState(initial = listOf())

        val appliedPalette by viewModel.getApplied().collectAsState(initial = listOf())

        ThemeChooserView (
            onClose ={
                navController.popBackStack()
            },
            appliedPalette = appliedPalette.firstOrNull(),
            palettes = allPalette,
            onSelectColorPalette = { new , old ->
                viewModel.updateSelectedColor(new, old)
            }
        )

    }
}