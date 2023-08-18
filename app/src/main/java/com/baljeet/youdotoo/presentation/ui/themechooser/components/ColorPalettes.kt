package com.baljeet.youdotoo.presentation.ui.themechooser.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.addHapticFeedback
import com.baljeet.youdotoo.common.getSampleColorPalette
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue


@Composable
fun ColorPalettes(
    modifier: Modifier,
    palette : ColorPaletteEntity,
    appliedPalette : ColorPaletteEntity,
    circleSize : Int = 50,
    onSelectPalette :() -> Unit
) {

    val hapticFeedback = LocalHapticFeedback.current


    Column(
        modifier = modifier
            .height((circleSize + 50).dp)
            .width((circleSize + circleSize).dp)
            .padding(10.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ){

            Box(
                modifier= Modifier
                    .width(circleSize.dp)
                    .height(circleSize.dp)
                    .align(Alignment.CenterStart)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(circleSize.dp)
                    )
                    .background(
                        color = Color(palette.nightDark),
                        shape = RoundedCornerShape(circleSize.dp)
                    )
            )
            Box(
                modifier= Modifier
                    .width(circleSize.dp)
                    .height(circleSize.dp)
                    .align(Alignment.CenterEnd)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(circleSize.dp)
                    )
                    .background(
                        color = Color(palette.nightLight),
                        shape = RoundedCornerShape(circleSize.dp)
                    )

            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = {
                    addHapticFeedback(hapticFeedback = hapticFeedback)
                    onSelectPalette()
                },
                modifier = Modifier

                    .height(30.dp)
                    .width(30.dp)
                    .padding(0.dp),
            ) {
                if (palette.paletteName == appliedPalette.paletteName) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Checked circular icon",
                        tint =  NightDotooBrightBlue,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = "Checked circular icon",
                        tint =  NightDotooBrightBlue,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewColorPalettes(){
    ColorPalettes(
        modifier = Modifier,
        palette = getSampleColorPalette(),
        onSelectPalette = {},
        appliedPalette = getSampleColorPalette()
    )
}