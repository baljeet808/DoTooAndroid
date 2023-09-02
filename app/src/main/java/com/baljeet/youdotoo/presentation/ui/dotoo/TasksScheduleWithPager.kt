package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baljeet.youdotoo.common.DashboardTaskTabs
import com.baljeet.youdotoo.common.EnumDashboardTasksTabs
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.presentation.ui.projects.components.TasksSchedulesTabRow
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TasksScheduleWithPager(
    onToggleTask : (TaskWithProject) -> Unit,
    navigateToQuickEditTaskTitle : (TaskWithProject) -> Unit,
    navigateToEditTask : (TaskWithProject) -> Unit,
    modifier: Modifier,
    onTaskDelete: (TaskWithProject) -> Unit
) {

    SharedPref.init(LocalContext.current)

    val viewModel : TasksScheduleViewModel = hiltViewModel()

    val allTasks by viewModel.getAllTasksWithProjectAsFlow().collectAsState(initial = listOf())

    val todayDate = java.time.LocalDateTime.now().toKotlinLocalDateTime()

    val todayDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault()).epochSeconds


    val tomorrowDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault())
        .plus(
            unit = DateTimeUnit.DAY,
            value = 1,
            timeZone = TimeZone.currentSystemDefault()
        ).epochSeconds

    val yesterdayDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault())
        .minus(
            unit = DateTimeUnit.DAY,
            value = 1,
            timeZone = TimeZone.currentSystemDefault()
        ).epochSeconds

    val yesterdayTasks = allTasks.filter { task -> task.task.dueDate == yesterdayDateInLong }
    val todayTasks = allTasks.filter { task -> task.task.dueDate == todayDateInLong }
    val tomorrowTasks = allTasks.filter { task -> task.task.dueDate == tomorrowDateInLong }
    val pendingTasks = allTasks.filter { task -> task.task.dueDate < yesterdayDateInLong }
    val allOtherTasks = allTasks.filter { task -> task.task.dueDate > tomorrowDateInLong }

    val tasksTabs = getTaskTabs(
        todayTasks = todayTasks,
        tomorrowTasks = tomorrowTasks,
        pendingTasks = pendingTasks,
        yesterdayTasks = yesterdayTasks,
        allOtherTasks = allOtherTasks,
    )


    val startingTabIndex = 0
    val pagerState = rememberPagerState(initialPage = startingTabIndex)

    /**
     * Logic to check if need to show empty view or not
     * **/
    var showNothingHereView by remember { mutableStateOf(false) }
    showNothingHereView = (tasksTabs[pagerState.currentPage].taskCount == 0)


    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        /**
         * Empty view
         * **/
        AnimatedVisibility(visible = showNothingHereView) {
            NothingHereView(
                modifier = Modifier.padding(bottom = 100.dp)
            )
        }

        /**
         * Main Content
         * **/
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {

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
            ) { currentIndex ->
                AnimatedVisibility(visible = tasksTabs[currentIndex].tasks.isNotEmpty()) {
                    DoTooItemsLazyColumn(
                        doToos = tasksTabs[currentIndex].tasks.toCollection(ArrayList()),
                        onToggleDoToo = onToggleTask,
                        navigateToEditTask = navigateToEditTask,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                top = 10.dp,
                                start = 10.dp,
                                end = 10.dp
                            ),
                        onItemDelete = { task ->
                            onTaskDelete(task)
                        },
                        navigateToQuickEditTask = navigateToQuickEditTaskTitle
                    )
                }

            }
        }
    }
}


fun getTaskTabs(
    pendingTasks: List<TaskWithProject>,
    yesterdayTasks: List<TaskWithProject>,
    todayTasks: List<TaskWithProject>,
    tomorrowTasks: List<TaskWithProject>,
    allOtherTasks: List<TaskWithProject>
): ArrayList<DashboardTaskTabs> {

    val tasksTabs = arrayListOf<DashboardTaskTabs>()

    EnumDashboardTasksTabs.values().forEachIndexed { index, enumDashboardTasksTabs ->
        when (enumDashboardTasksTabs) {
            EnumDashboardTasksTabs.Today -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Today,
                        taskCount = todayTasks.size,
                        tasks = todayTasks
                    )
                )
            }

            EnumDashboardTasksTabs.Tomorrow -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Tomorrow,
                        taskCount = tomorrowTasks.size,
                        tasks = tomorrowTasks
                    )
                )
            }

            EnumDashboardTasksTabs.Yesterday -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Yesterday,
                        taskCount = yesterdayTasks.size,
                        tasks = yesterdayTasks
                    )
                )
            }

            EnumDashboardTasksTabs.Pending -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.Pending,
                        taskCount = pendingTasks.size,
                        tasks = pendingTasks
                    )
                )
            }

            EnumDashboardTasksTabs.AllOther -> {
                tasksTabs.add(
                    DashboardTaskTabs(
                        index = index,
                        name = EnumDashboardTasksTabs.AllOther,
                        taskCount = allOtherTasks.size,
                        tasks = allOtherTasks
                    )
                )
            }
        }
    }
    return tasksTabs
}

