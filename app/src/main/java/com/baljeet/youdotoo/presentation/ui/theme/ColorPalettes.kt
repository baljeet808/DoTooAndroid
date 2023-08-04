package com.baljeet.youdotoo.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getRandomColor


@Composable
fun ColorPalettes(
    modifier: Modifier,
    darkColor : Color = Color(getRandomColor()),
    lightColor : Color = Color(getRandomColor()),
    circleSize : Int = 50
) {

    Box(
        modifier = modifier
            .height((circleSize+20).dp)
            .width((circleSize+circleSize).dp)
            .padding(10.dp)
    ){

        Box(
            modifier= Modifier
                .width(circleSize.dp)
                .height(circleSize.dp)
                .align(Alignment.CenterStart)
                .background(
                    color = darkColor,
                    shape = RoundedCornerShape(circleSize.dp)
                )
        )
        Box(
            modifier= Modifier
                .width(circleSize.dp)
                .height(circleSize.dp)
                .align(Alignment.CenterEnd)
                .background(
                    color = lightColor,
                    shape = RoundedCornerShape(circleSize.dp)
                )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewColorPalettes(){
    ColorPalettes(
        modifier = Modifier
    )
}