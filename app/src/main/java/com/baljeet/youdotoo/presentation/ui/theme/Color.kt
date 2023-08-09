package com.baljeet.youdotoo.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.baljeet.youdotoo.common.SharedPref

val DotooPink = Color(0xFFFD555D)
val DotooOrange = Color(0xFFFDA437)
val DotooBlue = Color(0xFF424F82)
val DotooDarkerGray = Color(0xFF1D1D1D)
val DoTooLightBlue = Color(0xFFD6DAE2)

//used in light theme
val LightDotooLightBlue = Color(0xFFBDC0CB)
val LightDotooTextColor = Color(0xFF1C2136)
val LightDotooFooterTextColor = Color(0xFFBDC0CB)
val LightAppBarIconsColor = Color(0xFF959DB8)


//used in dark theme
val NightDotooBrightPink = Color(0xFFEB06FF)
val NightDotooBrightBlue = Color(0xFF0C71FF)
val NightDotooLightBlue = Color(0xFF4D7199)
val NightDotooTextColor = Color(0xFFFFFFFF)
val NightDotooFooterTextColor = Color(0xFF4D7199)
val NightAppBarIconsColor = Color(0xFF69839E)
val NightTransparentWhiteColor = Color(0x11FFFFFF)
val LessTransparentWhiteColor = Color(0x99FFFFFF)
val LessTransparentBlueColor = Color(0x11041955)


val DoTooYellow = Color(0xFFFF8526)
val DoTooRed = Color(0xFFF53C4F)


@Composable
fun getDarkThemeColor(): Color{
   return if(isSystemInDarkTheme()){
        getNightDarkColor()
    }else{
        getDayDarkColor()
   }
}

@Composable
fun getLightThemeColor(): Color{
   return if(isSystemInDarkTheme()){
       getNightLightColor()
   }else{
       getDayLightColor()
   }
}

@Composable
fun getNightDarkColor(): Color{
    return Color(SharedPref.themeNightDarkColor)
}
@Composable
fun getNightLightColor(): Color{
    return Color(SharedPref.themeNightLightColor)
}
@Composable
fun getDayDarkColor(): Color{
    return Color(SharedPref.themeDayDarkColor)
}
@Composable
fun getDayLightColor(): Color{
    return Color(SharedPref.themeDayLightColor)
}
@Composable
fun getThemePaletteName(): String{
    return SharedPref.selectedColorPalette
}

@Composable
fun getTextColor(): Color{
    return if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
}