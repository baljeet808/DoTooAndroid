package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleProjectWithTaskCount
import com.baljeet.youdotoo.presentation.ui.projects.ProjectWithTaskCount
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue


@Composable
fun ProjectCardView(
    project: ProjectWithTaskCount,
    role: String,
    onItemClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .widthIn(max = 220.dp)
            .heightIn(max = 140.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(20.dp)
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
                    text = project.taskCount.toString().plus(" Tasks"),
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
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            LinearProgressIndicator(
                progress = project.progress,
                modifier = Modifier,
                color = NightDotooBrightPink,
                strokeCap = StrokeCap.Round
            )
        }

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun DefaultProjectCardPreview() {
    ProjectCardView(
        project = getSampleProjectWithTaskCount(),
        onItemClick = {},
        role = "Blocked"
    )
}
