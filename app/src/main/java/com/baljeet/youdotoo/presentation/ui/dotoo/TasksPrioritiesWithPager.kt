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
import com.baljeet.youdotoo.common.DashboardTaskTabsByPriorities
import com.baljeet.youdotoo.common.EnumPriorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TasksPrioritiesWithPager(
    onToggleTask : (TaskWithProject) -> Unit,
    navigateToQuickEditTaskTitle : (TaskWithProject) -> Unit,
    navigateToEditTask : (TaskWithProject) -> Unit,
    modifier: Modifier,
    onTaskDelete: (TaskWithProject) -> Unit
) {

    SharedPref.init(LocalContext.current)

    val viewModel : TasksScheduleViewModel = hiltViewModel()

    val allTasks by viewModel.getAllTasksWithProjectAsFlow().collectAsState(initial = listOf())

    val tasksTabs = getPrioritiesTabs(allTasks)

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
            TasksPrioritiesTabRow(
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


fun getPrioritiesTabs(
    allTasks: List<TaskWithProject>
): ArrayList<DashboardTaskTabsByPriorities> {
    val tasksTabs = arrayListOf<DashboardTaskTabsByPriorities>()
    EnumPriorities.values().forEachIndexed { index, priority ->
        val tasks  = allTasks.filter { task -> task.task.priority == priority.toString }
        tasksTabs.add(
            DashboardTaskTabsByPriorities(
                index = index,
                name = priority,
                taskCount = tasks.size,
                tasks = tasks
            )
        )
    }
    return tasksTabs
}

