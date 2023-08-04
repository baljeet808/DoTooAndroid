package com.baljeet.youdotoo.presentation.ui.projects

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectsLazyRow
import com.baljeet.youdotoo.presentation.ui.projects.components.TasksSchedulesTabRow
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.baljeet.youdotoo.presentation.ui.theme.*
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projects: List<ProjectWithDoToos>,
    pendingTasks: List<DoTooItem>,
    yesterdayTasks: List<DoTooItem>,
    todayTasks: List<DoTooItem>,
    tomorrowTasks: List<DoTooItem>,
    allOtherTasks: List<DoTooItem>,
    userId: String,
    userName: String,
    onToggleTask: (DoTooItem) -> Unit,
    navigateToTask: (DoTooItem) -> Unit,
    navigateToCreateTask: () -> Unit,
    navigateToCreateProject: () -> Unit,
    deleteTask: (task: DoTooItem) -> Unit
) {

    val projectsListState = rememberLazyListState()
    val listMoveScope = rememberCoroutineScope()
    val moveAnimationDelay : Long = 200

    val tasksTabs = EnumDashboardTasksTabs.values()

    val startingTabIndex = 0

    val pagerState = rememberPagerState(initialPage = startingTabIndex)

    val pendingLazyListState = rememberLazyListState()
    val yesterdayLazyListState = rememberLazyListState()
    val todayLazyListState = rememberLazyListState()
    val tomorrowLazyListState = rememberLazyListState()
    val allOtherLazyListState = rememberLazyListState()

    val isTopCardsVisible = isTopCardVisible(
        pendingLazyListState,
        yesterdayLazyListState,
        todayLazyListState,
        tomorrowLazyListState,
        allOtherLazyListState,
        pagerState
    )

    val transition = rememberInfiniteTransition()

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
    Scaffold(
        floatingActionButton = {
            androidx.compose.material.FloatingActionButton(
                onClick = navigateToCreateTask,
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
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightNormalThemeColor
                    } else {
                        DotooGray
                    }
                )
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

                /**
                 * Top Row for greeting and Add project button
                 * **/
                AnimatedVisibility(visible = isTopCardsVisible) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        /**
                         * Greeting text
                         * **/
                        Text(
                            text = if (userName.length > 8) {
                                "Hi, $userName!"
                            } else {
                                "What's up, $userName!"
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            fontFamily = FontFamily(Nunito.ExtraBold.font),
                            fontSize = 38.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        /**
                         * Create Project Button
                         * **/
                        FilledIconButton(
                            onClick = navigateToCreateProject,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isSystemInDarkTheme()) {
                                    NightDarkThemeColor
                                } else {
                                    NightNormalThemeColor
                                }
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)

                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add list button",
                                tint = Color.White
                            )
                        }
                    }
                }

                /**
                 * Simple Projects heading
                 * **/
                AnimatedVisibility(visible = isTopCardsVisible) {
                    Text(
                        text = "Projects".uppercase(),
                        color = LightAppBarIconsColor,
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        letterSpacing = TextUnit(2f, TextUnitType.Sp)
                    )
                }


                /**
                 * Horizontal list of all projects
                 * **/
                AnimatedVisibility(visible = isTopCardsVisible) {
                    ProjectsLazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        projects = projects.sortedBy { p ->  p.project.updatedAt }.reversed(),
                        navigateToDoToos = navigateToDoToos,
                        userId = userId,
                        listState = projectsListState
                    )
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
                     * Logic to check if need to show empty view or not
                     * **/
                    var showNothingHereView by remember { mutableStateOf(false) }
                    showNothingHereView = when (pagerState.currentPage) {
                        0 -> {
                            todayTasks.isEmpty()
                        }
                        1 -> {
                            tomorrowTasks.isEmpty()
                        }
                        2 -> {
                            yesterdayTasks.isEmpty()
                        }
                        3 -> {
                            pendingTasks.isEmpty()
                        }
                        4 -> {
                            allOtherTasks.isEmpty()
                        }
                        else -> allOtherTasks.isEmpty()
                    }

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
                            .fillMaxSize()
                    ) {

                        /**
                         *Tab row view
                         * **/
                        TasksSchedulesTabRow(
                            pagerState = pagerState,
                            tasksTabs = tasksTabs,
                            todayTasksCount = todayTasks.size,
                            tomorrowTasksCount = tomorrowTasks.size,
                            yesterdayTasksCount = yesterdayTasks.size,
                            pendingTasksCount = pendingTasks.size,
                            allOtherTasksCount = allOtherTasks.size
                        )

                        /**
                         * Tasks pages by schedule
                         * **/
                        HorizontalPager(
                            count = tasksTabs.size,
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            when (tasksTabs[pagerState.currentPage]) {
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
                                            onNavigateClick = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                                            lazyListState = yesterdayLazyListState,
                                            onItemDelete = { task ->
                                                Log.v("Log for - ", "reached projects view")
                                                deleteTask(task)
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
                                            onNavigateClick = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                                            lazyListState = todayLazyListState,
                                            onItemDelete = { task ->
                                                Log.v("Log for - ", "reached projects view")
                                                deleteTask(task)
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
                                            onNavigateClick = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                                            lazyListState = tomorrowLazyListState,
                                            onItemDelete = { task ->
                                                deleteTask(task)
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
                                            onNavigateClick = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                                            lazyListState = pendingLazyListState,
                                            onItemDelete = { task ->
                                                Log.v("Log for - ", "reached projects view")
                                                deleteTask(task)
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
                                            onNavigateClick = { doToo ->
                                                navigateToTask(doToo)
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                                            lazyListState = allOtherLazyListState,
                                            onItemDelete = { task ->
                                                deleteTask(task)
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
    }
}

@OptIn(ExperimentalPagerApi::class)
fun isTopCardVisible(
    pendingLazyListState: LazyListState,
    yesterdayLazyListState: LazyListState,
    todayLazyListState: LazyListState,
    tomorrowLazyListState: LazyListState,
    allOtherLazyListState: LazyListState,
    pagerState: PagerState
): Boolean {
    when (pagerState.currentPage) {
        0 -> {
            return pendingLazyListState.isScrolled.not()
        }
        1 -> {
            return yesterdayLazyListState.isScrolled.not()
        }
        2 -> {
            return todayLazyListState.isScrolled.not()
        }
        3 -> {
            return tomorrowLazyListState.isScrolled.not()
        }
        4 -> {
            return allOtherLazyListState.isScrolled.not()
        }
        else -> {
            return true
        }
    }
}


fun Project.getUserRole(userId: String): String {
    if (this.ownerId == userId) {
        return "Admin"
    }
    if (this.collaboratorIds.contains(userId)) {
        return "Collaborator"
    }
    if (this.viewerIds.contains(userId)) {
        return "Viewer"
    }
    return "Blocked"
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
        userId = "",
        userName = "Karandeep Kaur",
        onToggleTask = {},
        navigateToTask = {},
        navigateToCreateTask = {},
        navigateToCreateProject = {},
        deleteTask = {}
    )
}
