package com.baljeet.youdotoo.presentation.ui.projectsonly

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun  ProjectsOnlyView(
    projects : List<ProjectWithDoToos>,
    onClose: () -> Unit
) {
    SharedPref.init(LocalContext.current)

    var showBlur by remember {
        mutableStateOf(false)
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = if(isSystemInDarkTheme()){
            getDarkThemeColor()
        }else{
            getNightLightColor()
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if(isSystemInDarkTheme()){
                    getDarkThemeColor()
                }else{
                    getNightLightColor()
                }
            ).blur(
                radius = if (showBlur) {
                    20.dp
                } else {
                    0.dp
                }
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ){

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, end = 10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){

            item{
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /**
                     * Close icon button
                     * **/
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .border(
                                width = 1.dp,
                                color = getLightThemeColor(),
                                shape = RoundedCornerShape(40.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.ArrowBackIos,
                            contentDescription = "Button to close current screen.",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    /**
                     * Title
                     * **/
                    Text(
                        text = "All Projects",
                        modifier = Modifier
                            .padding(15.dp)
                            .weight(1f),
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        fontSize = 28.sp,
                        color = Color.White
                    )

                }

            }

            items(projects, key = {p -> p.project.id}){item ->
                ProjectCardWithProfiles(
                    project = item.project.toProject(),
                    users = listOf(),
                    tasks = item.tasks,
                    onItemDeleteClick = {  },
                    updateProjectTitle = {},
                    updateProjectDescription = {},
                    toggleNotificationSetting = {},
                    onClickInvite = {},
                    showFullCardInitially = false,
                    showDialogBackgroundBlur = {
                        showBlur = it
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

        }


    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectsOnlyView(){
    ProjectsOnlyView(
        projects = listOf(
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks()
        ),
        onClose ={}
    )
}