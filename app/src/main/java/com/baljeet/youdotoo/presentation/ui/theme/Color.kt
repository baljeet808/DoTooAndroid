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
val DotooDarkerGray = Color(0xFF1D1D1D)
val DotooDarkGray = Color(0xFF2E2E30)
val OnDotooDarkGray = Color(0xFF2B2326)
val DotooGray = Color(0xFFF1F2F8)
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
val NightAppBarIconsColor = Color(0xFF69839E)
val NightTransparentWhiteColor = Color(0x11FFFFFF)
val LessTransparentWhiteColor = Color(0x77FFFFFF)
val LessTransparentBlueColor = Color(0x11041955)


//common
val DoTooRed = Color(0xFFF53C4F)
val DoTooYellow = Color(0xFFFF8526)
val DotooGreen = Color(0xFFB7E885)
val DotooSkyBlue= Color(0xFF8BDFFE)
val DotooTeal= Color(0xFFA6F0E0)
val DotooGraphite= Color(0xFF302D2D)
val DotooPeach= Color(0xFFE8869F)
val DotooPurple= Color(0xFFBE89FE)
val DotooDarkGreen= Color(0xFF006261)
val DotooPeakYellow= Color(0xFFF2CD31)
val DotooCuteGreen= Color(0xFF2D9959)


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