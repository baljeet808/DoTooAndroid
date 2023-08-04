package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.addHapticFeedback
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.playWooshSound
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.NightDarkThemeColor

@Composable
fun DoTooItemView(
    doToo: DoTooItem,
    onNavigateClick: () -> Unit,
    onToggleDone: () -> Unit
) {

    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDarkThemeColor
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onNavigateClick)
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            IconButton(
                onClick = {
                    if (doToo.done.not()) {
                        playWooshSound(context)
                    }
                    addHapticFeedback(hapticFeedback = hapticFeedback)
                    onToggleDone()
                },
                modifier = Modifier

                    .height(30.dp)
                    .width(30.dp)
                    .padding(0.dp),
            ) {
                if (doToo.done) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Checked circular icon",
                        tint = /*if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            LightAppBarIconsColor
                        }*/ Color(doToo.projectColor),
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = "Checked circular icon",
                        tint = /*NightDotooBrightBlue*/ Color(doToo.projectColor),
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = doToo.title,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                },
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = if (doToo.done) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                },
                modifier = Modifier
                    .weight(0.9f)
            )

        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDoTooItemView() {
    DoTooItemView(
        doToo = getSampleDotooItem(),
        onToggleDone = {},
        onNavigateClick = {}
    )
}