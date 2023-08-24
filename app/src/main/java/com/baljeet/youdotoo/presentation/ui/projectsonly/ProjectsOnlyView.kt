package com.baljeet.youdotoo.presentation.ui.projectsonly

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor


@Composable
fun  ProjectsOnlyView(
    projects : List<ProjectWithDoToos>,
    goBack : () -> Unit
) {
    SharedPref.init(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
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
                Text(
                    text = "All Projects",
                    modifier = Modifier
                        .padding(15.dp)
                        .weight(1f),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 28.sp,
                    color = getTextColor()
                )
            }

            items(projects, key = {p -> p.project.id}){item ->
                ProjectCardWithProfiles(
                    project = item.project.toProject(),
                    users = listOf(),
                    tasks = item.tasks.map { it.toDoTooItem() },
                    onItemDeleteClick = {  },
                    updateProjectTitle = {},
                    updateProjectDescription = {},
                    toggleNotificationSetting = {},
                    onClickInvite = {},
                    showFullCardInitially = false
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
        goBack = {}
    )
}