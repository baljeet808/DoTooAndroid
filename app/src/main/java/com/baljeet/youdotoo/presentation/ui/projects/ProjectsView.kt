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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
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
import com.baljeet.youdotoo.common.DashboardTaskTabs
import com.baljeet.youdotoo.common.EnumDashboardTasksTabs
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsLazyRow
import com.baljeet.youdotoo.presentation.ui.projects.components.TasksSchedulesTabRow
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBoxRound
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentBlueColor
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projects: List<ProjectWithDoToos>,
    pendingTasks: List<DoTooItem>,
    yesterdayTasks: List<DoTooItem>,
    todayTasks: List<DoTooItem>,
    tomorrowTasks: List<DoTooItem>,
    allOtherTasks: List<DoTooItem>,
    onToggleTask: (DoTooItem) -> Unit,
    navigateToTask: (DoTooItem) -> Unit,
    navigateToCreateTask: () -> Unit,
    navigateToCreateProject: () -> Unit,
    deleteTask: (task: DoTooItem) -> Unit,
    updateTaskTitle: (task: DoTooItem, title: String) -> Unit
) {

    SharedPref.init(LocalContext.current)

    var taskToEdit: DoTooItem? = null

    val projectsListState = rememberLazyListState()
    val listMoveScope = rememberCoroutineScope()
    val moveAnimationDelay: Long = 200

    var showBlur by remember {
        mutableStateOf(false)
    }

    val tasksTabs = getTaskTabs(
        allOtherTasks = allOtherTasks,
        todayTasks = todayTasks,
        tomorrowTasks = tomorrowTasks,
        pendingTasks = pendingTasks,
        yesterdayTasks = yesterdayTasks
    )


    val startingTabIndex = 0

    val pagerState = rememberPagerState(initialPage = startingTabIndex)


    /**
     * Logic to check if need to show empty view or not
     * **/
    var showNothingHereView by remember { mutableStateOf(false) }
    showNothingHereView = ((tasksTabs[0].taskCount == 0) && (pagerState.currentPage == 0))


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


                /**
                 * Box with tasks view layer and empty view layer
                 * **/
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {


                    /**
                     * Empty view
                     * **/
                    androidx.compose.animation.AnimatedVisibility(visible = showNothingHereView) {
                        NothingHereView(
                            modifier = Modifier.padding(bottom = 100.dp)
                        )
                    }


                    /**
                     * Tasks divided in tabs by schedule
                     * **/
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.End
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            TextButton(
                                onClick = { showTopInfo = showTopInfo.not() },
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                Text(
                                    text = if (showTopInfo) {
                                        "Show less"
                                    } else {
                                        "Show more"
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

                            AnimatedVisibility(visible = showTopInfo.not()) {

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
                        }


                        /**
                         *Tab row view
                         * **/
                        TasksSchedulesTabRow(
                            pagerState = pagerState,
                            tasksTabs = tasksTabs
                        )

                        /**
                         * Tasks pages by schedule
                         * **/
                        HorizontalPager(
                            count = tasksTabs.size,
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                        ) {currentIndex ->
                            when (tasksTabs[currentIndex].name) {
                                EnumDashboardTasksTabs.Yesterday -> {
                                    AnimatedVisibility(visible = yesterdayTasks.isNotEmpty()) {
                                        DoTooItemsLazyColumn(
                                            doToos = yesterdayTasks,
                                            onToggleDoToo = { doToo ->
                                                onToggleTask(doToo)
                                                listMoveScope.launch {
                                                    delay(moveAnimationDelay)
                                                    projectsListState.animateScrollToItem(0)
                                                }
                                            },
                                            navigateToEditTask = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 10.dp,
                                                    end = 10.dp
                                                ),
                                            onItemDelete = { task ->
                                                if (yesterdayTasks.size == 1) {
                                                    focusScope.launch {
                                                        pagerState.scrollToPage(page = 0)
                                                    }
                                                }
                                                deleteTask(task)
                                            },
                                            navigateToQuickEditTask = {
                                                taskToEdit = it
                                                keyBoardController?.show()
                                                showBlur = true
                                                focusScope.launch {
                                                    delay(500)
                                                    focusRequester.requestFocus()
                                                }
                                            }
                                        )
                                    }
                                }

                                EnumDashboardTasksTabs.Today -> {
                                    AnimatedVisibility(visible = todayTasks.isNotEmpty()) {
                                        DoTooItemsLazyColumn(
                                            doToos = todayTasks,
                                            onToggleDoToo = { doToo ->
                                                onToggleTask(doToo)
                                                listMoveScope.launch {
                                                    delay(moveAnimationDelay)
                                                    projectsListState.animateScrollToItem(0)
                                                }
                                            },
                                            navigateToEditTask = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 10.dp,
                                                    end = 10.dp
                                                ),
                                            onItemDelete = { task ->
                                                deleteTask(task)
                                            },
                                            navigateToQuickEditTask = {
                                                taskToEdit = it
                                                keyBoardController?.show()
                                                showBlur = true
                                                focusScope.launch {
                                                    delay(500)
                                                    focusRequester.requestFocus()
                                                }
                                            }
                                        )
                                    }
                                }

                                EnumDashboardTasksTabs.Tomorrow -> {
                                    AnimatedVisibility(visible = tomorrowTasks.isNotEmpty()) {
                                        DoTooItemsLazyColumn(
                                            doToos = tomorrowTasks,
                                            onToggleDoToo = { doToo ->
                                                onToggleTask(doToo)
                                                listMoveScope.launch {
                                                    delay(moveAnimationDelay)
                                                    projectsListState.animateScrollToItem(0)
                                                }
                                            },
                                            navigateToEditTask = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 10.dp,
                                                    end = 10.dp
                                                ),
                                            onItemDelete = { task ->
                                                if (tomorrowTasks.size == 1) {
                                                    focusScope.launch {
                                                        pagerState.scrollToPage(page = 0)
                                                    }
                                                }
                                                deleteTask(task)
                                            },
                                            navigateToQuickEditTask = {
                                                taskToEdit = it
                                                keyBoardController?.show()
                                                showBlur = true
                                                focusScope.launch {
                                                    delay(500)
                                                    focusRequester.requestFocus()
                                                }
                                            }
                                        )
                                    }
                                }

                                EnumDashboardTasksTabs.Pending -> {
                                    AnimatedVisibility(visible = pendingTasks.isNotEmpty()) {
                                        DoTooItemsLazyColumn(
                                            doToos = pendingTasks,
                                            onToggleDoToo = { doToo ->
                                                onToggleTask(doToo)
                                                listMoveScope.launch {
                                                    delay(moveAnimationDelay)
                                                    projectsListState.animateScrollToItem(0)
                                                }
                                            },
                                            navigateToEditTask = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 10.dp,
                                                    end = 10.dp
                                                ),
                                            onItemDelete = { task ->
                                                if (pendingTasks.size == 1) {
                                                    focusScope.launch {
                                                        pagerState.scrollToPage(page = 0)
                                                    }
                                                }
                                                deleteTask(task)
                                            },
                                            navigateToQuickEditTask = {
                                                taskToEdit = it
                                                keyBoardController?.show()
                                                showBlur = true
                                                focusScope.launch {
                                                    delay(500)
                                                    focusRequester.requestFocus()
                                                }
                                            }
                                        )
                                    }
                                }

                                EnumDashboardTasksTabs.AllOther -> {
                                    AnimatedVisibility(visible = allOtherTasks.isNotEmpty()) {
                                        DoTooItemsLazyColumn(
                                            doToos = allOtherTasks,
                                            onToggleDoToo = { doToo ->
                                                onToggleTask(doToo)
                                                listMoveScope.launch {
                                                    delay(moveAnimationDelay)
                                                    projectsListState.animateScrollToItem(0)
                                                }
                                            },
                                            navigateToEditTask = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(
                                                    top = 10.dp,
                                                    start = 10.dp,
                                                    end = 10.dp
                                                ),
                                            onItemDelete = { task ->
                                                if (allOtherTasks.size == 1) {
                                                    focusScope.launch {
                                                        pagerState.scrollToPage(page = 0)
                                                    }
                                                }
                                                deleteTask(task)
                                            },
                                            navigateToQuickEditTask = {
                                                taskToEdit = it
                                                keyBoardController?.show()
                                                showBlur = true
                                                focusScope.launch {
                                                    delay(500)
                                                    focusRequester.requestFocus()
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                }

            }
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


fun Project.getUserRole(): String {
    SharedPref.userId?.let { userId ->
        if (this.ownerId == userId) {
            return "Admin"
        }
        if (this.collaboratorIds.contains(userId)) {
            return "Collaborator"
        }
        if (this.viewerIds.contains(userId)) {
            return "Viewer"
        }
    }
    return "Blocked"
}


fun getTaskTabs(
    pendingTasks: List<DoTooItem>,
    yesterdayTasks: List<DoTooItem>,
    todayTasks: List<DoTooItem>,
    tomorrowTasks: List<DoTooItem>,
    allOtherTasks: List<DoTooItem>
): ArrayList<DashboardTaskTabs> {

    val tasksTabs = arrayListOf<DashboardTaskTabs>()

    EnumDashboardTasksTabs.values().forEachIndexed { index, enumDashboardTasksTabs ->
        when (enumDashboardTasksTabs) {
            EnumDashboardTasksTabs.Today -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Today,
                        taskCount = todayTasks.size
                    )
                )
            }

            EnumDashboardTasksTabs.Tomorrow -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Tomorrow,
                        taskCount = tomorrowTasks.size
                    )
                )
            }

            EnumDashboardTasksTabs.Yesterday -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Yesterday,
                        taskCount = yesterdayTasks.size
                    )
                )
            }

            EnumDashboardTasksTabs.Pending -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Pending,
                        taskCount = pendingTasks.size
                    )
                )
            }

            EnumDashboardTasksTabs.AllOther -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.AllOther,
                        taskCount = allOtherTasks.size
                    )
                )
            }
        }
    }
    return tasksTabs
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
        pendingTasks = arrayListOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        ),
        yesterdayTasks = arrayListOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        ),
        todayTasks = arrayListOf(
            /*getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()*/
        ),
        tomorrowTasks = arrayListOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        ),
        allOtherTasks = arrayListOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        ),
        onToggleTask = {},
        navigateToTask = {},
        navigateToCreateTask = {},
        navigateToCreateProject = {},
        deleteTask = {},
        updateTaskTitle = { _, _ -> },

    )
}
