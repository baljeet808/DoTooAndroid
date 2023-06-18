package com.baljeet.youdotoo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DotooPink = Color(0xFFFD555D)
val DotooHalfTransparentPink = Color(0x55FD555D)
val DotooOrange = Color(0xFFFDA437)
val DotooHalfTransparentOrange = Color(0x55FDA437)
val DotooBlue = Color(0xFF3278F9)
val DotooHalfTransparentBlue = Color(0x553278F9)
val DotooGreen = Color(0xFF69BA6C)
val DotooHalfTransparentGreen = Color(0x5569BA6C)
val DotooDarkGray = Color(0xFF2E2E30)
val OnDotooDarkGray = Color(0xFF2B2326)
val DotooGray = Color(0xFFF8F1F3)
val OnDotooGray = Color(0xFFDDD0D4)
val HighPriorityColor = Color(0xFFF44336)
val MediumPriorityColor = Color(0xFF9C27B0)
val LowPriorityColor = Color(0xFF69BA6C)
val CurrentlyWorkingPriorityColor = Color(0xFFFF9800)
val WorkDebtPriorityColor = Color(0xFFE91E63)

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