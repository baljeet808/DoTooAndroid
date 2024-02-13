package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getColor
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor


@Composable
fun ProjectCardView(
    project: ProjectWithDoToos,
    role: EnumRoles,
    onItemClick: () -> Unit,
    modifier: Modifier,
    usingForDemo: Boolean = false,
    hideProjectTasksFromDashboard : (project: ProjectEntity) -> Unit
) {

    SharedPref.init(LocalContext.current)

    val animatedProgress = if (usingForDemo) {
        animateFloatAsState(
            targetValue = .4f,
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
        ).value
    } else {
        animateFloatAsState(
            targetValue = (project.tasks.filter { task -> task.done }.size.toFloat() / (project.tasks.size).toFloat()),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
        ).value
    }

    Column(
        modifier = modifier
            .widthIn(
                max = if (usingForDemo) {
                    150.dp
                } else 220.dp
            )
            .heightIn(
                max = if (usingForDemo) {
                    90.dp
                } else 160.dp
            )
            .shadow(
                elevation = 5.dp, shape = RoundedCornerShape(
                    if (usingForDemo) {
                        6.dp
                    } else 10.dp
                )
            )
            .background(
                color = getDarkThemeColor(),
                shape = RoundedCornerShape(
                    if (usingForDemo) {
                        6.dp
                    } else 10.dp
                )
            ),
    ) {

        Row(
            modifier = Modifier
                .background(
                    color = project.project.color.getColor()
                )
                .fillMaxWidth()
                .height(
                    if (usingForDemo) {
                        7.dp
                    } else 12.dp
                )
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onItemClick)
                .padding(
                    start = if (usingForDemo) {
                        7.dp
                    } else 15.dp,
                    end = if (usingForDemo) {
                        7.dp
                    } else 15.dp,
                    bottom = if (usingForDemo) {
                        7.dp
                    } else 15.dp,
                    top = if (usingForDemo) {
                        2.dp
                    } else 5.dp
                ),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = if (usingForDemo) {
                            2.dp
                        } else 5.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = project.tasks.size.toString()
                        .plus(if (project.tasks.size == 1) " Task" else " Tasks"),
                    color = getTextColor(),
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = if (usingForDemo) {
                        9.sp
                    } else 16.sp,
                    modifier = Modifier
                )

                Text(
                    text = role.name,
                    color = getTextColor(),
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = if (usingForDemo) {
                        9.sp
                    } else 16.sp,
                    modifier = Modifier
                )
            }

            Text(
                text = project.project.name,
                color = getTextColor(),
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = if (usingForDemo) {
                    14.sp
                } else 20.sp,
                maxLines = 1,
                minLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            )
            Spacer(
                modifier = Modifier.height(
                    if (usingForDemo) {
                        2.dp
                    } else 5.dp
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {hideProjectTasksFromDashboard(project.project)},
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp)
                ) {
                    Icon(
                        if(project.project.hideFromDashboard) (Icons.Default.VisibilityOff) else (Icons.Default.Visibility),
                        contentDescription = "Show/hide project from dashboard button",
                        tint = Color.Gray
                    )
                }

            }
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
fun DefaultProjectCardPreview() {
    ProjectCardView(
        modifier = Modifier,
        project = getSampleProjectWithTasks(),
        onItemClick = {},
        role = EnumRoles.Editor,
        hideProjectTasksFromDashboard = {}
    )
}
