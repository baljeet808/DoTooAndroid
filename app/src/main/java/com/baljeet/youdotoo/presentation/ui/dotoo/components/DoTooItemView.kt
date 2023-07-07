package com.baljeet.youdotoo.presentation.ui.dotoo.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.datetime.*
import java.time.LocalDateTime

@Composable
fun DoTooItemView(
    doToo: DoTooItem,
    onNavigateClick: () -> Unit,
    onToggleDone: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onNavigateClick)
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(20.dp)
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            IconButton(
                onClick = onToggleDone,
                modifier = Modifier

                    .height(30.dp)
                    .width(30.dp)
                    .padding(0.dp),
            ) {
                if (doToo.done) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Checked circular icon",
                        tint = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            LightAppBarIconsColor
                        },
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = "Checked circular icon",
                        tint = NightDotooBrightBlue,
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
                fontSize = 20.sp,
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
        doToo = DoTooItem(
            id = "",
            title = "Need to water the plants ASAP!",
            description = "Plants are getting dry, we need to water them today. Who ever is home please do this.",
            dueDate = LocalDateTime.now().toKotlinLocalDateTime()
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            createDate = LocalDateTime.now().toKotlinLocalDateTime()
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            done = true,
            priority = "High",
            updatedBy = "Baljeet Singh created this task."
        ),
        onToggleDone = {},
        onNavigateClick = {}
    )
}