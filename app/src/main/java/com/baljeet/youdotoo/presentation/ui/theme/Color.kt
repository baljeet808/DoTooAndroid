package com.baljeet.youdotoo.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DotooPink = Color(0xFFFD555D)
val DotooOrange = Color(0xFFFDA437)
val DotooBlue = Color(0xFF424F82)
val DotooDarkerGray = Color(0xFF1D1D1D)
val DotooDarkGray = Color(0xFF2E2E30)
val OnDotooDarkGray = Color(0xFF2B2326)
val DotooGray = Color(0xFFF1F2F8)
val DoTooLightBlue = Color(0xFFD6DAE2)
val OnDotooGray = Color(0xFFDDD0D4)

//used in light theme
val LightDotooLightBlue = Color(0xFFBDC0CB)
val LightDotooTextColor = Color(0xFF1C2136)
val LightDotooFooterTextColor = Color(0xFFBDC0CB)
val LightAppBarIconsColor = Color(0xFF959DB8)


//used in dark theme
val NightDarkThemeColor = Color(0xFF0A0A0A) //Color(0xFF041955)
val NightNormalThemeColor = Color(0xFF282A29) //Color(0xFF3450A1)
val NightDotooBrightPink = Color(0xFFEB06FF)
val NightDotooBrightBlue = Color(0xFF0C71FF)
val NightDotooLightBlue = Color(0xFF4D7199)
val NightDotooTextColor = Color(0xFFFFFFFF)
val NightDotooFooterTextColor = Color(0xFF4D7199)
val NightAppBarIconsColor = Color(0xFF69839E)
val NightTransparentWhiteColor = Color(0x11FFFFFF)
val LessTransparentWhiteColor = Color(0x99FFFFFF)
val LessTransparentBlueColor = Color(0x11041955)

val NightDotooEerieBlack = Color(0xFF0A0A0A)
val NightDotooJet = Color(0xFF282A29)

val DoTooYellow = Color(0xFFFF8526)
val DoTooRed = Color(0xFFF53C4F)


@Composable
fun getCardColor():Color{
    return if(isSystemInDarkTheme()){
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
fun getTextColor(): Color{
    return if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }
}
