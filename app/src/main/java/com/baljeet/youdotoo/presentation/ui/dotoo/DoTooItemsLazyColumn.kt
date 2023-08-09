package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun DoTooItemsLazyColumn(
   // lazyListState: LazyListState,
    doToos: List<DoTooItem>,
    onToggleDoToo: (doToo: DoTooItem) -> Unit,
    onNavigateClick: (doToo: DoTooItem) -> Unit,
    navigateToEditTask : (task: DoTooItem) -> Unit,
    onItemDelete: (doToo: DoTooItem) -> Unit,
    modifier: Modifier
) {



    val parentJob = Job()
    val stopScope = rememberCoroutineScope()

    LazyColumn(
       // state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(doToos, key = {it.id}) { dotoo ->

            var startJob : CoroutineScope? = null

            val state = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        startJob = CoroutineScope(parentJob)
                        startJob?.launch {
                            delay(1000)
                            onItemDelete(dotoo)
                        }
                    }
                    true
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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.SpaceAround,
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
                    Text(
                        text = "UNDO",
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        },
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        letterSpacing = TextUnit(1.5f, TextUnitType.Sp),
                        modifier = Modifier.clickable(
                            onClick = {
                                startJob?.cancel()
                                stopScope.launch {
                                    state.reset()
                                }
                            }
                        )
                    )
                }
            },
                dismissContent = {
                    DoTooItemView(
                        doToo = dotoo,
                        onNavigateClick = {
                            onNavigateClick(dotoo)
                        },
                        onToggleDone = {
                            onToggleDoToo(dotoo)
                        },
                        lastItem = doToos.last() == dotoo,
                        navigateToEditDotoo = {
                            navigateToEditTask(dotoo)
                        }
                    )
                },
                directions = setOf(DismissDirection.EndToStart))
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDoTooItemsLazyColumn() {
    val sampleTaskItem = getSampleDotooItem()
    DoTooItemsLazyColumn(
       // lazyListState = LazyListState(),
        doToos = listOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem(),
            sampleTaskItem
        ),
        onNavigateClick = {},
        onToggleDoToo = {},
        modifier = Modifier,
        onItemDelete = {},
        navigateToEditTask = {}
    )
}