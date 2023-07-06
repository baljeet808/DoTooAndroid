package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor


@Composable
fun ProjectCardView(
    project: Project,
    role: String,
    onItemClick: () -> Unit
) {


    val transition = rememberInfiniteTransition()

    val offsetX by transition.animateValue(
        initialValue = (180).dp,
        targetValue = 80.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    val offsetY by transition.animateValue(
        initialValue = (100).dp,
        targetValue = 50.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )

    val offsetX1 by transition.animateValue(
        initialValue = 0.dp,
        targetValue = 80.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    val offsetY1 by transition.animateValue(
        initialValue = 0.dp,
        targetValue = 100.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )

    Box(
        modifier = Modifier
            .widthIn(max = 220.dp)
            .heightIn(max = 160.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    NightDotooNormalBlue
                },
                shape = RoundedCornerShape(20.dp)
            ),
    ) {

        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 60.dp.toPx(),
                center = Offset(
                    x = offsetX1.toPx(),
                    y = offsetY1.toPx()
                )
            )
        })

        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 30.dp.toPx(),
                center = Offset(
                    x = offsetX.toPx(),
                    y = offsetY.toPx()
                )
            )
        })

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onItemClick)
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    when (role) {
                        "Admin" -> Icons.Default.Star
                        "Collaborator" -> Icons.Default.Groups
                        "Viewer" -> Icons.Default.Visibility
                        else -> Icons.Default.Block
                    },
                    contentDescription = "Icon to show that user is $role of selected project.",
                    tint = Color.White,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            }

            Text(
                text = project.name,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = 0.6f,
                modifier = Modifier
                    .padding(5.dp),
                color = NightDotooBrightPink,
                strokeCap = StrokeCap.Round
            )
        }

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultProjectCardPreview() {
    ProjectCardView(
        project = Project(
            id = "",
            name = "Home Chores",
            description = "This project is about the irritating stuff which always gets forgotten.",
            ownerId = "",
            collaboratorIds = arrayListOf(),
            viewerIds = arrayListOf(),
            update = ""
        ),
        onItemClick = {},
        role = "Blocked"
    )
}
