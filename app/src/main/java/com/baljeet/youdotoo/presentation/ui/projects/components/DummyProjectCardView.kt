package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getColor
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightAppBarIconsColor


@Composable
fun DummyProjectCardView(
    project: ProjectWithDoToos,
    role: String,
    onItemClick: () -> Unit,
    modifier: Modifier,
    backgroundColor: Color = getRandomColor().getColor(),
    textColor: Color = Color.White,
    leftAlign: Boolean
) {

    val animatedProgress = animateFloatAsState(
        targetValue = (project.tasks.filter { task -> task.done }.size.toFloat() / (project.tasks.size).toFloat()),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value

    Box(
        modifier = modifier
            .widthIn(max = 220.dp)
            .heightIn(max = 160.dp)
            .shadow(
                elevation = 5.dp, shape = if (leftAlign) {
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 0.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 20.dp
                    )
                }
            )
            .background(
                color = backgroundColor,
                shape = if (leftAlign) {
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 20.dp,
                        bottomEnd = 20.dp,
                        bottomStart = 0.dp
                    )
                } else {
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 20.dp
                    )
                }
            ),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onItemClick)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = project.tasks.size.toString().plus(" Tasks"),
                    color = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    },
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    modifier = Modifier
                )

                Icon(
                    when (role) {
                        "Admin" -> Icons.Default.Star
                        "Collaborator" -> Icons.Default.Groups
                        "Viewer" -> Icons.Default.Visibility
                        else -> Icons.Default.Block
                    },
                    contentDescription = "Icon to show that user is $role of selected project.",
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        LightAppBarIconsColor
                    },
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )


            }

            Text(
                text = project.project.name,
                color = textColor,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 20.sp,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier,
                color = project.project.color.getColor(),
                strokeCap = StrokeCap.Round,
            )
        }

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDummyProjectCardPreview() {
    DummyProjectCardView(
        modifier = Modifier,
        project = getSampleProjectWithTasks(),
        onItemClick = {},
        role = "Blocked",
        backgroundColor = getRandomColor().getColor(),
        textColor = Color.White,
        leftAlign = true
    )
}
