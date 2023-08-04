package com.baljeet.youdotoo.presentation.ui.project

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.*
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.theme.*

@Composable
fun ProjectView(
    project: ProjectEntity?,
    userId : String,
    users : List<UserEntity>,
    tasks : List<DoTooItemEntity>,
    onToggle : (doTooItem : DoTooItem, project : Project ) -> Unit,
    navigateToCreateTask : (projectOwner : Boolean) -> Unit,
    deleteTask : (DoTooItem) -> Unit,
    deleteProject : () -> Unit,
    upsertProject : (Project)  -> Unit,
    onClickInvite : () -> Unit
) {

    val lazyListState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToCreateTask(
                        project?.ownerId == userId
                    )
                },
                modifier = Modifier,
                backgroundColor = NightDotooBrightBlue
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Floating button to quickly add a task to this project",
                    tint = Color.White
                )
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightNormalThemeColor
                    } else {
                        DotooGray
                    }
                ),
            verticalArrangement = Arrangement.Top
        ) {

            /**
             *Top Project Card
             * **/
            ProjectCardWithProfiles(
                project = project?.toProject(),
                users = users.map { it.toUser() },
                tasks = tasks.map { it.toDoTooItem() },
                lazyListState = lazyListState,
                onItemDeleteClick = deleteProject,
                updateProjectTitle = { title ->
                    project?.copy()?.toProject()?.let { projectCopy ->
                        projectCopy.name = title
                        upsertProject(projectCopy)
                    }
                },
                updateProjectDescription = { description ->
                    project?.copy()?.toProject()?.let { projectCopy ->
                        projectCopy.description = description
                        upsertProject(projectCopy)
                    }
                },
                toggleFavorite = {},
                toggleNotificationSetting = {},
                onClickInvite = onClickInvite
            )

            /**
             * List of tasks form this project
             * **/
            DoTooItemsLazyColumn(
                lazyListState = lazyListState,
                doToos = tasks.map { it.toDoTooItem() },
                onToggleDoToo = {doToo->
                    project?.let {
                        onToggle(doToo, project.toProject())
                    }
                },
                onNavigateClick = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp),
                onItemDelete = deleteTask
            )

        }
    }



}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectView() {
    ProjectView(
        project = getSampleProject().toProjectEntity(),
        onToggle = {_,_->},
        navigateToCreateTask = {},
        userId = getRandomId(),
        tasks = listOf(
            getSampleDotooItem().toDoTooItemEntity(getRandomId())
        ),
        users = listOf(getSampleProfile().toUserEntity()),
        deleteTask = {},
        deleteProject = {},
        upsertProject = {},
        onClickInvite = {}
    )
}