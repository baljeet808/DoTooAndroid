package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.DashboardTaskTabsByPriorities
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TasksPrioritiesTabRow(
    pagerState: PagerState,
    tasksTabs : ArrayList<DashboardTaskTabsByPriorities>
) {

    val darkTheme = isSystemInDarkTheme()

    val scope = rememberCoroutineScope()

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
                Animatable(Color.Black)
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

                        AnimatedVisibility(visible = tasksTab.taskCount > 0) {
                            Text(
                                text = tasksTab.taskCount.toString(),
                                color = color.value,
                                modifier = Modifier,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = tasksTab.name.toString,
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

}