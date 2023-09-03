package com.baljeet.youdotoo.presentation.ui.themechooser

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Updated by Baljeet singh.
 * **/

const val DestinationThemeChooserRoute = "ThemeChooser"


fun NavGraphBuilder.addThemeChooserViewDestination(
    navController: NavHostController
) {
    composable(
        route = DestinationThemeChooserRoute
    ) {
        val viewModel: ColorPalettesViewModel = hiltViewModel()

        val allPalette by viewModel.getColorPalettes().collectAsState(initial = listOf())

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = if (isSystemInDarkTheme()) {
                getDarkThemeColor()
            } else {
                getNightLightColor()
            }
        )

        ThemeChooserView(
            onClose = {
                navController.popBackStack()
            },
            palettes = allPalette,
            onSelectColorPalette = { palette ->
                SharedPref.themePaletteId = palette.id
                SharedPref.themeDayDarkColor = palette.dayDark
                SharedPref.themeDayLightColor = palette.dayLight
                SharedPref.themeNightDarkColor = palette.nightDark
                SharedPref.themeNightLightColor = palette.nightLight
                SharedPref.selectedColorPalette = palette.paletteName
                navController.popBackStack()
            }
        )

    }
}