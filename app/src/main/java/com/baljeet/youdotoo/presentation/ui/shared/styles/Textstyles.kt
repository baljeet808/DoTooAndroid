package com.baljeet.youdotoo.presentation.ui.shared.styles

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import com.baljeet.youdotoo.R


enum class Nunito(val font : Font){
    Normal (Font(R.font.alata, FontWeight.Normal)),
    SemiBold (Font(R.font.alata, FontWeight.SemiBold)),
    Bold  (Font(R.font.alata, FontWeight.Bold)),
    ExtraBold (Font(R.font.alata, FontWeight.ExtraBold)),
}