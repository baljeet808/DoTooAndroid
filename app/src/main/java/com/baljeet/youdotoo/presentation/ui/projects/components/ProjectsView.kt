package com.baljeet.youdotoo.presentation.ui.projects.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.components.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.baljeet.youdotoo.presentation.ui.theme.*
import com.google.accompanist.pager.*
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

    val tasksTabs = EnumDashboardTasksTabs.values()

    val startingTabIndex = 0


    /**
     * This logic did not pan out, keeping it to fix it in future
     * **/
    /*val tomorrowUndoneTasksCount = tomorrowTasks.filter { t -> t.done.not() }.size
    val todayUndoneTasksCount = todayTasks.filter { t -> t.done.not() }.size
    val yesterdayUndoneTasksCount = yesterdayTasks.filter { t -> t.done.not() }.size
    val pendingUndoneTasksCount = pendingTasks.filter { t -> t.done.not() }.size
    val allOtherUndoneTasksCount = allOtherTasks.filter { t -> t.done.not() }.size

    var maxUndoneTasksCount = todayUndoneTasksCount

    for (i in 1..5) {
        if (tomorrowUndoneTasksCount > maxUndoneTasksCount){
            startingTabIndex = 1
            maxUndoneTasksCount = tomorrowUndoneTasksCount
        }
        if (yesterdayUndoneTasksCount > maxUndoneTasksCount){
            startingTabIndex = 2
            maxUndoneTasksCount = yesterdayUndoneTasksCount
        }
        if (pendingUndoneTasksCount > maxUndoneTasksCount){
            startingTabIndex = 3
            maxUndoneTasksCount = pendingUndoneTasksCount
        }
        if (allOtherUndoneTasksCount > maxUndoneTasksCount){
            startingTabIndex = 4
            maxUndoneTasksCount = allOtherUndoneTasksCount
        }
    }*/

    val pagerState = rememberPagerState(initialPage = startingTabIndex)

    val scope = rememberCoroutineScope()

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
        typeConverter = Dp.VectorConverter
    )
    val offsetY by transition.animateValue(
        initialValue = (450).dp,
        targetValue = 550.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )

    val offsetX1 by transition.animateValue(
        initialValue = 160.dp,
        targetValue = 310.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    val offsetY1 by transition.animateValue(
        initialValue = 550.dp,
        targetValue = 450.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
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
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDotooNormalBlue
                    } else {
                        DotooGray
                    }
                )
        ) {


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

                        FilledIconButton(
                            onClick = navigateToCreateProject,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isSystemInDarkTheme()) {
                                    NightDotooDarkBlue
                                } else {
                                    NightDotooNormalBlue
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

                AnimatedVisibility(visible = isTopCardsVisible) {
                    /**
                     * Simple Projects heading
                     * **/
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
                        projects = projects,
                        navigateToDoToos = navigateToDoToos,
                        userId = userId
                    )
                }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    androidx.compose.material.ScrollableTabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier
                            .padding(0.dp),
                        indicator = { tabPositions ->
                            TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier
                                    .pagerTabIndicatorOffset(
                                        pagerState,
                                        tabPositions
                                    )
                            )
                        },
                        backgroundColor = Color.Transparent,
                        divider = {}
                    ) {
                        tasksTabs.forEachIndexed { index, tasksTab ->

                            val color = remember {
                                androidx.compose.animation.Animatable(Color.Black)
                            }
                            LaunchedEffect(key1 = pagerState.currentPage == index) {
                                color.animateTo(
                                    if (pagerState.currentPage == index) {
                                        if (darkTheme) {
                                            Color.White
                                        } else {
                                            Color.Black
                                        }
                                    } else {
                                        LightAppBarIconsColor
                                    }
                                )
                            }
                            androidx.compose.material.Tab(
                                text = {
                                    Row(
                                        modifier = Modifier,
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        val tasksCount = when (tasksTabs.indexOf(tasksTab)) {
                                            0 -> todayTasks.size
                                            1 -> tomorrowTasks.size
                                            2 -> yesterdayTasks.size
                                            3 -> pendingTasks.size
                                            4 -> allOtherTasks.size
                                            else -> allOtherTasks.size
                                        }

                                        AnimatedVisibility(visible = tasksCount > 0) {
                                            Text(
                                                text = tasksCount.toString(),
                                                color = color.value,
                                                modifier = Modifier,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = if (tasksTab == EnumDashboardTasksTabs.AllOther) "All Other" else tasksTab.name,
                                            color = color.value,
                                            fontFamily = FontFamily(Nunito.Normal.font),
                                            letterSpacing = TextUnit(1f, TextUnitType.Sp)
                                        )
                                    }
                                },
                                selected = pagerState.currentPage == index,
                                modifier = Modifier.background(color = Color.Transparent),
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }


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
                                AnimatedVisibility(visible = yesterdayTasks.isEmpty()) {
                                }
                            }
                            EnumDashboardTasksTabs.Today -> {
                                AnimatedVisibility(visible = todayTasks.isNotEmpty()) {
                                    DoTooItemsLazyColumn(
                                        doToos = todayTasks,
                                        onToggleDoToo = { doToo ->
                                            onToggleTask(doToo)
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
                                AnimatedVisibility(visible = todayTasks.isEmpty()) {
                                    NothingHereView()
                                }
                            }
                            EnumDashboardTasksTabs.Tomorrow -> {
                                AnimatedVisibility(visible = tomorrowTasks.isNotEmpty()) {
                                    DoTooItemsLazyColumn(
                                        doToos = tomorrowTasks,
                                        onToggleDoToo = { doToo ->
                                            onToggleTask(doToo)
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
                                AnimatedVisibility(visible = tomorrowTasks.isEmpty()) {

                                }
                            }
                            EnumDashboardTasksTabs.Pending -> {
                                AnimatedVisibility(visible = pendingTasks.isNotEmpty()) {
                                    DoTooItemsLazyColumn(
                                        doToos = pendingTasks,
                                        onToggleDoToo = { doToo ->
                                            onToggleTask(doToo)
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

                                AnimatedVisibility(visible = pendingTasks.isEmpty()) {
                                }
                            }
                            EnumDashboardTasksTabs.AllOther -> {
                                AnimatedVisibility(visible = allOtherTasks.isNotEmpty()) {
                                    DoTooItemsLazyColumn(
                                        doToos = allOtherTasks,
                                        onToggleDoToo = { doToo ->
                                            onToggleTask(doToo)
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

                                AnimatedVisibility(visible = allOtherTasks.isEmpty()) {

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
