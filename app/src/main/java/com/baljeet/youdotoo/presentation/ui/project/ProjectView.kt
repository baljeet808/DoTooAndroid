package com.baljeet.youdotoo.presentation.ui.project

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.data.mappers.toUser
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBoxRound
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProjectView(
    project: ProjectEntity?,
    users: List<UserEntity>,
    tasks: List<DoTooItemEntity>,
    onToggle: (doTooItem: DoTooItem, project: Project) -> Unit,
    navigateToCreateTask: () -> Unit,
    deleteTask: (DoTooItem) -> Unit,
    deleteProject: () -> Unit,
    upsertProject: (Project) -> Unit,
    onClickInvite: () -> Unit,
    navigateToEditTask: (task: DoTooItem) -> Unit,
    navigateToChat: () -> Unit,
    updateTaskTitle: (task: DoTooItem, title: String) -> Unit
) {

    SharedPref.init(LocalContext.current)



    var taskToEdit: DoTooItem? = null


    var showBlur by remember {
        mutableStateOf(false)
    }

    val focusScope = rememberCoroutineScope()
    val keyBoardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateTask,
                modifier = Modifier,
                backgroundColor = project?.color?.let{Color(it)}?: NightDotooBrightBlue
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Floating button to quickly add a task to this project",
                    tint = Color.White
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = getLightThemeColor()
                )
                .blur(
                    radius = if (showBlur) {
                        20.dp
                    } else {
                        0.dp
                    }
                )
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {

            /**
             *Top Project Card
             * **/
            ProjectCardWithProfiles(
                project = project?.toProject(),
                users = users.map { it.toUser() },
                tasks = tasks.map { it.toDoTooItem() },
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
                toggleNotificationSetting = {},
                onClickInvite = onClickInvite
            )

            AnimatedVisibility(visible = users.isNotEmpty()) {
                TextButton(
                    onClick = navigateToChat ,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(
                        text = "Chat",
                        fontFamily = FontFamily(Nunito.Normal.font)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = "show less or more button"
                    )
                }
            }


            /**
             * List of tasks form this project
             * **/
            DoTooItemsLazyColumn(
                doToos = tasks.map { it.toDoTooItem() },
                onToggleDoToo = { doToo ->
                    project?.let {
                        onToggle(doToo, project.toProject())
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp),
                onItemDelete = deleteTask,
                navigateToEditTask = { task ->
                    navigateToEditTask(task)
                },
                navigateToQuickEditTask = { task ->
                    taskToEdit = task
                    keyBoardController?.show()
                    showBlur = true
                    focusScope.launch {
                        delay(500)
                        focusRequester.requestFocus()
                    }
                }
            )

        }


        AnimatedVisibility(
            visible = showBlur && taskToEdit != null,
            enter = slideInVertically(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = EaseIn
                ),
                initialOffsetY = {
                    it / 2
                }
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = EaseOut
                ),
                targetOffsetY = {
                    it / 2
                }
            )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = padding)
                    .clickable(
                        onClick = {
                            showBlur = false
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                EditOnFlyBoxRound(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    onSubmit = { title ->
                        updateTaskTitle(taskToEdit!!, title)
                        keyBoardController?.hide()
                        showBlur = false
                    },
                    placeholder = taskToEdit?.title ?: "",
                    label = "Edit Task",
                    maxCharLength = maxTitleCharsAllowed,
                    onCancel = {
                        showBlur = false
                    },
                    themeColor = Color(taskToEdit?.projectColor ?: getRandomColor()),
                    lines = 2
                )

            }
        }

    }


}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectView() {
    ProjectView(
        project = getSampleProject().toProjectEntity(),
        onToggle = { _, _ -> },
        navigateToCreateTask = {},
        tasks = listOf(
            getSampleDotooItem().toDoTooItemEntity(getRandomId())
        ),
        users = listOf(getSampleProfile().toUserEntity()),
        deleteTask = {},
        deleteProject = {},
        upsertProject = {},
        onClickInvite = {},
        navigateToEditTask = {},
        navigateToChat = {},
        updateTaskTitle = {_,_ ->}
    )
}