package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun DoTooItemsLazyColumn(
    doToos: ArrayList<TaskWithProject>,
    onToggleDoToo: (doToo: TaskWithProject) -> Unit,
    navigateToQuickEditTask : (task: TaskWithProject) -> Unit,
    navigateToEditTask : (task : TaskWithProject) -> Unit,
    onItemDelete: (doToo: TaskWithProject) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(doToos, key = {it.task.id}) { dotoo ->

            val state = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onItemDelete(dotoo)
                    }
                    SharedPref.deleteTaskWithoutConfirmation
                }
            )

            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(),
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
                        Text(
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
                        doToo = dotoo,
                        onToggleDone = {
                            onToggleDoToo(dotoo)
                        },
                        navigateToQuickEditDotoo = {
                            navigateToQuickEditTask(dotoo)
                        },
                        navigateToTaskEdit = {
                            navigateToEditTask(dotoo)
                        }
                    )
                },
                directions = setOf(
                    DismissDirection.EndToStart
                )
            )

        }

        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDoTooItemsLazyColumn() {
    DoTooItemsLazyColumn(
        doToos = arrayListOf(
            getSampleTaskWithProject(),
            getSampleTaskWithProject(),
            getSampleTaskWithProject(),
            getSampleTaskWithProject()
        ),
        onToggleDoToo = {},
        modifier = Modifier,
        onItemDelete = {},
        navigateToEditTask = {},
        navigateToQuickEditTask = {}
    )
}