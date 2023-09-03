package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.toLocalDateTime
import com.baljeet.youdotoo.common.toNiceDateFormat
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.NothingHereView
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksScheduleLazyColumn(
    modifier: Modifier,
    navigateToQuickEditTask : (task: TaskWithProject) -> Unit,
    navigateToEditTask : (task : TaskWithProject) -> Unit,
    toggleTask : (task : TaskWithProject) -> Unit,
    onDeleteTask : (task : TaskWithProject) -> Unit
) {

    val todayDate = java.time.LocalDateTime.now()
        .toKotlinLocalDateTime()

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




    SharedPref.init(LocalContext.current)

    val viewModel : TasksScheduleViewModel = hiltViewModel()

    val allTasksWithProjects by viewModel.getAllTasksWithProjectAsFlow().collectAsState(initial = listOf())

    val filteredTasks = if(SharedPref.doNotShowViewerTasksOnDashboard){
        allTasksWithProjects
            .filter {
                    task -> getRole(task.projectEntity.toProject()) != EnumRoles.Viewer
            }
    }else{
        allTasksWithProjects
    }

    /**
     * Logic to check if need to show empty view or not
     * **/
    var showNothingHereView by remember { mutableStateOf(false) }
    showNothingHereView = filteredTasks.isEmpty()


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
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {


            filteredTasks.filter { task -> task.task.dueDate < yesterdayDateInLong }.let { pendingTasks ->

                if (pendingTasks.isNotEmpty()) {
                    item {
                        HeadingTextWithCount(
                            heading = "Pending",
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    items(pendingTasks, key = { it.task.id }) { task ->
                        SwipeAbleTaskItemView(
                            task = task,
                            modifier = Modifier.animateItemPlacement(),
                            onToggleTask = {
                                toggleTask(task)
                            },
                            navigateToQuickEditTask = {
                                navigateToQuickEditTask(task)
                            },
                            navigateToEditTask = {
                                navigateToEditTask(task)
                            },
                            onDeleteTask = {
                                onDeleteTask(task)
                            }
                        )

                    }
                }
            }

            filteredTasks.filter { task -> task.task.dueDate == yesterdayDateInLong }.let { yesterdayTasks ->

                if (yesterdayTasks.isNotEmpty()) {

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        HeadingTextWithCount(
                            heading = "Yesterday",
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    items(yesterdayTasks, key = { it.task.id }) { task ->
                        SwipeAbleTaskItemView(
                            task = task,
                            modifier = Modifier.animateItemPlacement(),
                            onToggleTask = {
                                toggleTask(task)
                            },
                            navigateToQuickEditTask = {
                                navigateToQuickEditTask(task)
                            },
                            navigateToEditTask = {
                                navigateToEditTask(task)
                            },
                            onDeleteTask = {
                                onDeleteTask(task)
                            }
                        )

                    }
                }
            }
            filteredTasks.filter { task -> task.task.dueDate == todayDateInLong }.let { todayTasks ->

                if (todayTasks.isNotEmpty()) {

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        HeadingTextWithCount(
                            heading = "Today",
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    items(todayTasks, key = { it.task.id }) { task ->
                        SwipeAbleTaskItemView(
                            task = task,
                            modifier = Modifier.animateItemPlacement(),
                            onToggleTask = {
                                toggleTask(task)
                            },
                            navigateToQuickEditTask = {
                                navigateToQuickEditTask(task)
                            },
                            navigateToEditTask = {
                                navigateToEditTask(task)
                            },
                            onDeleteTask = {
                                onDeleteTask(task)
                            }
                        )

                    }
                }
            }
            filteredTasks.filter { task -> task.task.dueDate == tomorrowDateInLong }.let { tomorrowTasks ->

                if (tomorrowTasks.isNotEmpty()) {

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        HeadingTextWithCount(
                            heading = "Tomorrow",
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    items(tomorrowTasks, key = { it.task.id }) { task ->
                        SwipeAbleTaskItemView(
                            task = task,
                            modifier = Modifier.animateItemPlacement(),
                            onToggleTask = {
                                toggleTask(task)
                            },
                            navigateToQuickEditTask = {
                                navigateToQuickEditTask(task)
                            },
                            navigateToEditTask = {
                                navigateToEditTask(task)
                            },
                            onDeleteTask = {
                                onDeleteTask(task)
                            }
                        )

                    }
                }
            }
            filteredTasks.filter { task -> task.task.dueDate > tomorrowDateInLong }
                .sortedBy { task -> task.task.dueDate }.let { allOther ->

                allOther.distinctBy { task -> task.task.dueDate }.forEach { distinctByDate ->

                    allOther.filter { task -> task.task.dueDate == distinctByDate.task.dueDate }
                        .let { tasksOnSpecificDate ->
                            item {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            item {
                                HeadingTextWithCount(
                                    heading = distinctByDate.task.dueDate
                                        .toLocalDateTime()
                                        .toJavaLocalDateTime()
                                        .toLocalDate()
                                        .toNiceDateFormat(false),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            items(tasksOnSpecificDate, key = { it.task.id }) { task ->
                                SwipeAbleTaskItemView(
                                    task = task,
                                    modifier = Modifier.animateItemPlacement(),
                                    onToggleTask = {
                                        toggleTask(task)
                                    },
                                    navigateToQuickEditTask = {
                                        navigateToQuickEditTask(task)
                                    },
                                    navigateToEditTask = {
                                        navigateToEditTask(task)
                                    },
                                    onDeleteTask = {
                                        onDeleteTask(task)
                                    }
                                )
                            }
                        }
                }
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
fun HeadingTextWithCount(
    heading : String,
    modifier: Modifier
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = heading,
            color = getTextColor(),
            fontFamily = FontFamily(Nunito.Normal.font),
            letterSpacing = TextUnit(1f, TextUnitType.Sp)
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeAbleTaskItemView(
    task : TaskWithProject,
    modifier: Modifier,
    onToggleTask : () -> Unit,
    onDeleteTask : () -> Unit,
    navigateToEditTask: () -> Unit,
    navigateToQuickEditTask: () -> Unit
){
    val state = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToStart) {
                onDeleteTask()
            }
            SharedPref.deleteTaskWithoutConfirmation
        }
    )
    SwipeToDismiss(
        modifier = modifier,
        state = state,
        background = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(start = 20.dp, end = 20.dp)
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Deleted task icon",
                        tint = LightAppBarIconsColor,
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                    )
                    androidx.compose.material.Text(
                        text = "Task Deleted!",
                        color = LightAppBarIconsColor,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.Normal.font)
                    )
                }
            }
        },
        dismissContent = {
            DoTooItemView(
                doToo = task,
                onToggleDone = onToggleTask,
                navigateToQuickEditDotoo = {
                    navigateToQuickEditTask()
                },
                navigateToTaskEdit = {
                    navigateToEditTask()
                }
            )
        },
        directions = setOf(DismissDirection.EndToStart))
}