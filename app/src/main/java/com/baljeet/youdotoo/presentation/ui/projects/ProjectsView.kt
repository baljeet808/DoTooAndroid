package com.baljeet.youdotoo.presentation.ui.projects

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ListAlt
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.TasksScheduleLazyColumn
import com.baljeet.youdotoo.presentation.ui.dotoo.TasksScheduleWithPager
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsLazyRow
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.dialogs.AppCustomDialog
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBoxRound
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentBlueColor
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projects: List<ProjectWithDoToos>,
    onToggleTask: (DoTooItemEntity) -> Unit,
    navigateToTask: (DoTooItemEntity) -> Unit,
    navigateToCreateTask: () -> Unit,
    navigateToCreateProject: () -> Unit,
    deleteTask:(DoTooItemEntity) -> Unit,
    updateTaskTitle: (task: DoTooItemEntity, title: String) -> Unit
) {

    SharedPref.init(LocalContext.current)

    var showScheduleTasksView by remember {
        mutableStateOf(false)
    }

    var taskToEdit: DoTooItemEntity? = remember {
        null
    }

    var taskToDelete : DoTooItemEntity? = remember {
        null
    }

    val projectsListState = rememberLazyListState()
    val listMoveScope = rememberCoroutineScope()
    val moveAnimationDelay: Long = 200

    var showBlur by remember {
        mutableStateOf(false)
    }

    var showTopInfo by remember {
        mutableStateOf(true)
    }

    val transition = rememberInfiniteTransition(label = "")

    val darkTheme = isSystemInDarkTheme()

    val offsetX by transition.animateValue(
        initialValue = (300).dp,
        targetValue = 150.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )
    val offsetY by transition.animateValue(
        initialValue = (450).dp,
        targetValue = 550.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )

    val offsetX1 by transition.animateValue(
        initialValue = 160.dp,
        targetValue = 310.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )
    val offsetY1 by transition.animateValue(
        initialValue = 550.dp,
        targetValue = 450.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )

    val keyBoardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember {
        FocusRequester()
    }

    val focusScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            androidx.compose.material.FloatingActionButton(
                onClick = navigateToCreateTask,
                modifier = Modifier.height(50.dp),
                backgroundColor = getDarkThemeColor()
            ) {
                Row(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = "Floating button to quickly add a task to this project",
                        tint = getTextColor()
                    )
                    Text(
                        text = "Add Task",
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = getTextColor(),
                        fontSize = 14.sp
                    )
                }
            }
        }
    ) { padding ->


        Box(
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
                .padding(paddingValues = padding)
        ) {


            /**
             * Two Animated Circles in background
             * **/
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = if (darkTheme) {
                        NightTransparentWhiteColor
                    } else {
                        LessTransparentBlueColor
                    },
                    radius = 230.dp.toPx(),
                    center = Offset(
                        x = offsetX1.toPx(),
                        y = offsetY1.toPx()
                    )
                )
            })
            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = if (darkTheme) {
                        NightTransparentWhiteColor
                    } else {
                        LessTransparentBlueColor
                    },
                    radius = 180.dp.toPx(),
                    center = Offset(
                        x = offsetX.toPx(),
                        y = offsetY.toPx()
                    )
                )
            })


            /**
             * Main content on screen
             * **/
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {


                AnimatedVisibility(visible = showTopInfo) {
                    Column(modifier = Modifier.fillMaxWidth()) {


                        /**
                         * Greeting text
                         * **/
                        Text(
                            text = if (SharedPref.userName.length > 8) {
                                "Hi, ${SharedPref.userName}!"
                            } else {
                                "What's up, ${SharedPref.userName}!"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                            fontFamily = FontFamily(Nunito.ExtraBold.font),
                            fontSize = 38.sp,
                            color = getTextColor()
                        )

                        /**
                         * Top Row for greeting and Add project button
                         * **/
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            /**
                             * Simple Projects heading
                             * **/
                            Text(
                                text = "Projects".uppercase(),
                                color = LightAppBarIconsColor,
                                fontFamily = FontFamily(Nunito.Normal.font),
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .padding(5.dp),
                                letterSpacing = TextUnit(2f, TextUnitType.Sp)
                            )

                            /**
                             * Create Project Button
                             * **/

                            Row(
                                modifier = Modifier
                                    .height(40.dp)
                                    .background(
                                        color = getDarkThemeColor(),
                                        shape = RoundedCornerShape(60.dp)
                                    )
                                    .padding(start = 8.dp, end = 8.dp)
                                    .clickable(onClick = navigateToCreateProject),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Outlined.Add,
                                    contentDescription = "Floating button to add a project",
                                    tint = getTextColor()
                                )
                                Text(
                                    text = "Add Project",
                                    fontFamily = FontFamily(Nunito.Normal.font),
                                    color = getTextColor(),
                                    fontSize = 13.sp
                                )
                            }
                        }

                        /**
                         * Horizontal list of all projects
                         * **/
                        ProjectsLazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            projects = projects.sortedBy { p -> p.project.updatedAt }.reversed(),
                            navigateToDoToos = navigateToDoToos,
                            listState = projectsListState
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ) {

                    /**
                     * Show less/more button
                     * **/
                    TextButton(
                        onClick = { showTopInfo = showTopInfo.not() },
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = if (showTopInfo) {
                                "Hide projects"
                            } else {
                                "Show projects"
                            },
                            fontFamily = FontFamily(Nunito.Normal.font),
                            color = getTextColor()
                        )
                        Icon(
                            if (showTopInfo) {
                                Icons.Default.ExpandLess
                            } else {
                                Icons.Default.ExpandMore
                            },
                            contentDescription = "show less or more button",
                            tint = getTextColor()
                        )
                    }
                    TextButton(
                        onClick = { showScheduleTasksView = !showScheduleTasksView },
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Icon(
                            if (showScheduleTasksView) {
                                Icons.Default.ListAlt
                            } else {
                                Icons.Default.CalendarMonth
                            },
                            contentDescription = "Button to change task list style",
                            tint = getTextColor()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showScheduleTasksView) {
                                "Tabs View"
                            } else {
                                "Calendar View"
                            },
                            fontFamily = FontFamily(Nunito.Normal.font),
                            color = getTextColor()
                        )

                    }

                }

                /**
                 * Box with tasks view layer and empty view layer
                 * **/
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {


                    androidx.compose.animation.AnimatedVisibility(visible = showScheduleTasksView) {
                        TasksScheduleLazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 10.dp,
                                    start = 5.dp,
                                    end = 5.dp
                                ),
                            navigateToQuickEditTask = {
                                taskToEdit = it
                                keyBoardController?.show()
                                showBlur = true
                                focusScope.launch {
                                    delay(500)
                                    focusRequester.requestFocus()
                                }
                            },
                            navigateToEditTask = {
                                navigateToTask(it)
                            },
                            toggleTask = {
                                onToggleTask(it)
                                listMoveScope.launch {
                                    delay(moveAnimationDelay)
                                    projectsListState.animateScrollToItem(0)
                                }
                            },
                            onDeleteTask = { task ->
                                if(SharedPref.deleteTaskWithoutConfirmation){
                                    deleteTask(task)
                                }else{
                                    taskToDelete = task
                                    showBlur = true
                                }
                            }
                        )
                    }

                    androidx.compose.animation.AnimatedVisibility(visible = showScheduleTasksView.not()) {
                        /**
                         * Tasks divided in tabs by schedule
                         * **/
                        TasksScheduleWithPager(
                            navigateToQuickEditTaskTitle = {
                                taskToEdit = it
                                keyBoardController?.show()
                                showBlur = true
                                focusScope.launch {
                                    delay(500)
                                    focusRequester.requestFocus()
                                }
                            },
                            navigateToEditTask = {
                                navigateToTask(it)
                            },
                            onToggleTask = {
                                onToggleTask(it)
                                listMoveScope.launch {
                                    delay(moveAnimationDelay)
                                    projectsListState.animateScrollToItem(0)
                                }
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = 10.dp,
                                    start = 0.dp,
                                    end = 0.dp
                                ),
                            onTaskDelete = { task ->
                                if(SharedPref.deleteTaskWithoutConfirmation){
                                    deleteTask(task)
                                }else{
                                    taskToDelete = task
                                    showBlur = true
                                }
                            }
                        )
                    }

                }

            }
        }


        /**
         * Edit Task screen
         * **/
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
                            taskToEdit = null
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
                        taskToEdit = null
                    },
                    placeholder = taskToEdit?.title ?: "",
                    label = "Edit Task",
                    maxCharLength = maxTitleCharsAllowed,
                    onCancel = {
                        showBlur = false
                        taskToEdit = null
                    },
                    themeColor = Color(taskToEdit?.projectColor ?: getRandomColor()),
                    lines = 2
                )

            }
        }


        AnimatedVisibility(
            visible = showBlur && taskToDelete != null,
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
                            taskToDelete = null
                            showBlur = false
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                AppCustomDialog(
                    onDismiss = {
                        taskToDelete = null
                        showBlur = false
                    },
                    onConfirm = {
                        deleteTask(taskToDelete!!)
                        taskToDelete = null
                        showBlur = false
                    },
                    title = "Delete this task?",
                    description = "Are you sure, you want to permanently delete Following task? \n \"${taskToDelete?.title?:""}\"",
                    topRowIcon = Icons.Default.DeleteForever,
                    showDismissButton = true,
                    dismissButtonText = "Abort",
                    confirmButtonText = "Yes, proceed",
                    showCheckbox = SharedPref.deleteTaskWithoutConfirmation.not(),
                    checked = SharedPref.deleteTaskWithoutConfirmation,
                    onChecked = {
                           SharedPref.deleteTaskWithoutConfirmation = true
                    },
                    checkBoxText = "Delete without confirmation next time?",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                )
            }
        }

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultProjectPreview() {
    ProjectsView(
        navigateToDoToos = {},
        projects = arrayListOf(
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks()
        ),
        onToggleTask = {},
        navigateToTask = {},
        navigateToCreateTask = {},
        navigateToCreateProject = {},
        updateTaskTitle = { _, _ -> },
        deleteTask = {}
    )
}
