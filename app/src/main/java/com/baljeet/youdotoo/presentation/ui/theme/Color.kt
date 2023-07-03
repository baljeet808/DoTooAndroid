package com.baljeet.youdotoo.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DotooPink = Color(0xFFFD555D)
val DotooHalfTransparentPink = Color(0x55FD555D)
val DotooOrange = Color(0xFFFDA437)
val DotooHalfTransparentOrange = Color(0x55FDA437)
val DotooBlue = Color(0xFF424F82)
val DotooKaranBlue = Color(0xFF4D7199)
val DotooGreen = Color(0xFF69BA6C)
val DotooDarkerGray = Color(0xFF1D1D1D)
val DotooDarkGray = Color(0xFF2E2E30)
val OnDotooDarkGray = Color(0xFF2B2326)
val DotooGray = Color(0xFFF8F1F3)
val DoTooLightBlue = Color(0xFFD6DAE2)
val OnDotooGray = Color(0xFFDDD0D4)

//used in light theme
val LightDotooDarkBlue = Color(0xFF041955)
val LightDotooNormalBlue = Color(0xFF3450A1)
val LightDotooBrightPink = Color(0xFFEB06FF)
val LightDotooBrightBlue = Color(0xFF0C71FF)
val LightDotooLightBlue = Color(0xFFBDC0CB)
val LightDotooTextColor = Color(0xFF1C2136)
val LightDotooFooterTextColor = Color(0xFFBDC0CB)
val LightAppBarIconsColor = Color(0xFF959DB8)


//used in dark theme
val NightDotooDarkBlue = Color(0xFF041955)
val NightDotooNormalBlue = Color(0xFF3450A1)
val NightDotooBrightPink = Color(0xFFEB06FF)
val NightDotooBrightBlue = Color(0xFF0C71FF)
val NightDotooLightBlue = Color(0xFF4D7199)
val NightDotooTextColor = Color(0xFFFFFFFF)
val NightDotooFooterTextColor = Color(0xFF4D7199)
val NightAppBarIconsColor = Color(0xFF617B97)


@Composable
fun getCardColor():Color{
    return if(isSystemInDarkTheme()){
        DotooDarkGray
    }else{
        DotooGray
    }
}

@Composable
fun getOppositeCardColor():Color{
    return if(isSystemInDarkTheme().not()){
        DotooDarkGray
    }else{
        DotooGray
    }
}

@Composable
fun getOnCardColor():Color{
    return if(isSystemInDarkTheme()){
        OnDotooDarkGray
    }else{
        OnDotooGray
    }
}

@Composable
fun getOppositeOnCardColor():Color{
    return if(isSystemInDarkTheme().not()){
        OnDotooDarkGray
    }else{
        OnDotooGray
    }
}

@Composable
fun getTextColor(): Color{
    return if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
}

@Composable
fun getThemeColor(): Color{
    return if(isSystemInDarkTheme()){
        DotooBlue
    }else{
        DotooBlue
    }
}