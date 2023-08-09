package com.baljeet.youdotoo.presentation.ui.themechooser

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref

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


        ThemeChooserView (
            onClose ={
                navController.popBackStack()
            },
            appliedPalette = allPalette.firstOrNull { p -> p.paletteName == SharedPref.selectedColorPalette },
            palettes = allPalette,
            onSelectColorPalette = { new ->
                viewModel.updateSelectedColor(new)
            }
        )

    }
}