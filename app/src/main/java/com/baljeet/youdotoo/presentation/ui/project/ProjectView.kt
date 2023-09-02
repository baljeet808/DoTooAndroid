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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
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
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.data.mappers.toUser
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.dialogs.AppCustomDialog
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBoxRound
import com.baljeet.youdotoo.presentation.ui.theme.DoTooRed
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ProjectView(
    project: ProjectEntity,
    users: List<UserEntity>,
    tasks: List<TaskEntity>,
    onToggle: (TaskWithProject) -> Unit,
    navigateToCreateTask: () -> Unit,
    deleteTask: (TaskWithProject) -> Unit,
    deleteProject: () -> Unit,
    upsertProject: (Project) -> Unit,
    onClickInvite: () -> Unit,
    navigateToEditTask: (task: TaskWithProject) -> Unit,
    navigateToChat: () -> Unit,
    updateTaskTitle: (task: TaskWithProject, title: String) -> Unit
) {

    SharedPref.init(LocalContext.current)

    val showViewerPermissionDialog = remember {
        mutableStateOf(false)
    }

    val showProRequiredDialog = remember {
        mutableStateOf(false)
    }


    var showBlur by remember {
        mutableStateOf(false)
    }


    var taskToDelete : TaskWithProject? = remember {
        null
    }
    val showDeleteConfirmationDialog = remember {
        mutableStateOf(false)
    }

    if (showDeleteConfirmationDialog.value){
        AppCustomDialog(
            onDismiss = {
                taskToDelete = null
                showDeleteConfirmationDialog.value = false
                showBlur = false
            },
            onConfirm = {
                taskToDelete?.let(deleteTask)
                showDeleteConfirmationDialog.value = false
                showBlur = false
            },
            title = "Delete this task?",
            description = "Are you sure, you want to permanently delete Following task? \n \"${taskToDelete?.task?.title?:""}\"",
            topRowIcon = Icons.Default.DeleteForever,
            showDismissButton = true,
            dismissButtonText = "Abort",
            confirmButtonText = "Yes, proceed",
            showCheckbox = SharedPref.deleteTaskWithoutConfirmation.not(),
            onChecked = {
                SharedPref.deleteTaskWithoutConfirmation = true
            },
            checkBoxText = "Delete without confirmation next time?",
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }


    var taskToEdit: TaskWithProject? = null

    val role = getRole(project.toProject())

    val focusScope = rememberCoroutineScope()
    val keyBoardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }

    if (showViewerPermissionDialog.value){
        AppCustomDialog(
            onDismiss = {
                showBlur = false
                showViewerPermissionDialog.value = false
            },
            onConfirm = {
                showBlur = false
                showViewerPermissionDialog.value = false
            },
            title = "Permission Issue! 😣",
            description = "Sorry, you are a viewer in this project. And viewer can not create, edit, update or delete tasks. Ask Project Admin for permission upgrade.",
            topRowIcon = Icons.Default.Lock,
            onChecked = {  },
            showCheckbox = false,
            modifier = Modifier
        )
    }
    if (showProRequiredDialog.value && SharedPref.doNotBugMeAboutProFeaturesAgain.not()){
        AppCustomDialog(
            onDismiss = {
                showBlur = false
                showProRequiredDialog.value = false
            },
            onConfirm = {
                showBlur = false
                showProRequiredDialog.value = false
                //TODO: take user to pro features
            },
            title = "Pro feature 🦄",
            description = "Become a 👑 Pro member to share this project with your friends and chat with them about tasks.",
            topRowIcon = Icons.Default.Lock,
            onChecked = {
                SharedPref.doNotBugMeAboutProFeaturesAgain = true
            },
            showCheckbox = true,
            checkBoxText = "Do not ask me again!",
            modifier = Modifier,
            showDismissButton = true,
            dismissButtonText = "May be later",
            confirmButtonText = "More details"
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(getRole(project.toProject()) != EnumRoles.Viewer){
                        navigateToCreateTask()
                    }else{
                        showBlur = true
                        showViewerPermissionDialog.value = true
                    }
                },
                modifier = Modifier,
                backgroundColor = Color(project.color)
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
                project = project.toProject(),
                users = users.map { it.toUser() },
                tasks = tasks.map { it },
                onItemDeleteClick = deleteProject,
                updateProjectTitle = { title ->
                    project.copy().toProject().let { projectCopy ->
                        projectCopy.name = title
                        upsertProject(projectCopy)
                    }
                },
                updateProjectDescription = { description ->
                    project.copy().toProject().let { projectCopy ->
                        projectCopy.description = description
                        upsertProject(projectCopy)
                    }
                },
                toggleNotificationSetting = {},
                onClickInvite = onClickInvite,
                showDialogBackgroundBlur = {
                    showBlur = it
                }
            )


            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                TextButton(
                    onClick = {
                        if(role == EnumRoles.ProAdmin || role == EnumRoles.Editor || role == EnumRoles.Viewer){
                            navigateToChat()
                        }else{
                            showBlur = true
                            showProRequiredDialog.value = true
                        }
                    },
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(
                        text = "Chat",
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = getTextColor()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = "show less or more button",
                        tint = getTextColor()
                    )

                }
                if(role == EnumRoles.Admin) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Lock icon",
                        tint = DoTooRed,
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }
            }

            /**
             * List of tasks form this project
             * **/
            DoTooItemsLazyColumn(
                doToos = tasks.map { task ->
                    TaskWithProject(
                        task = task,
                        projectEntity = project
                    )
                }.toCollection(ArrayList()),
                onToggleDoToo = { task ->
                    if(role == EnumRoles.Viewer || role == EnumRoles.Blocked){
                        showBlur = true
                        showViewerPermissionDialog.value = true
                    }else {
                        onToggle(task)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp),
                onItemDelete = { task ->
                    if (role == EnumRoles.Viewer || role == EnumRoles.Blocked) {
                        showBlur = true
                        showViewerPermissionDialog.value = true
                    } else {
                        if (SharedPref.deleteTaskWithoutConfirmation) {
                            deleteTask(task)
                        } else {
                            taskToDelete = task
                            showBlur = true
                            showDeleteConfirmationDialog.value = true
                        }
                    }
                },
                navigateToEditTask = { task ->
                    if(role == EnumRoles.Viewer || role == EnumRoles.Blocked){
                        showBlur = true
                        showViewerPermissionDialog.value = true
                    }else {
                        navigateToEditTask(task)
                    }
                },
                navigateToQuickEditTask = { task ->
                    if(role == EnumRoles.Viewer || role == EnumRoles.Blocked){
                        showBlur = true
                        showViewerPermissionDialog.value = true
                    }else {
                        taskToEdit = task
                        keyBoardController?.show()
                        showBlur = true
                        focusScope.launch {
                            delay(500)
                            focusRequester.requestFocus()
                        }
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
                        taskToEdit?.let {
                            updateTaskTitle(it, title)
                        }
                        keyBoardController?.hide()
                        showBlur = false
                    },
                    placeholder = taskToEdit?.task?.title ?: "",
                    label = "Edit Task",
                    maxCharLength = maxTitleCharsAllowed,
                    onCancel = {
                        showBlur = false
                    },
                    themeColor = Color(taskToEdit?.projectEntity?.color ?: getRandomColor()),
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
        onToggle = { },
        navigateToCreateTask = {},
        tasks = listOf(
            getSampleDotooItem()
        ),
        users = listOf(getSampleProfile().toUserEntity()),
        deleteTask = {},
        deleteProject = {},
        upsertProject = {},
        onClickInvite = {},
        navigateToEditTask = {},
        navigateToChat = {},
        updateTaskTitle = { _, _ -> }
    )
}