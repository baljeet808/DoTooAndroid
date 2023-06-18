package com.baljeet.youdotoo.ui.dotoo

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.models.Project
import com.baljeet.youdotoo.models.ProjectWithProfiles
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.destinations.ChatViewDestination
import com.baljeet.youdotoo.ui.destinations.createDoTooViewDestination
import com.baljeet.youdotoo.ui.theme.*
import com.baljeet.youdotoo.util.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn( ExperimentalPagerApi::class)
@Destination
@Composable
fun DoTooView(
    navigator: DestinationsNavigator?,
    project: ProjectWithProfiles
) {
    val tabs = listOf(
        DoTooPriorityTab.All,
        DoTooPriorityTab.Today,
        DoTooPriorityTab.Done,
    )

    val viewModel = viewModel<DoToosViewModel>()
    val lazyListState = rememberLazyListState()
    val pagerState = rememberPagerState()

    val cardColor = getOnCardColor()
    val isDark = isSystemInDarkTheme()
    val scope = rememberCoroutineScope()


    val doToosStateList = remember { mutableStateListOf<DoTooWithProfiles>() }

    viewModel.init(project = project)
    LaunchedEffect(key1 = true) {
        viewModel.doToosState.collectLatest { state ->
            state.doToos?.let { dotoos ->
                doToosStateList.clear()
                doToosStateList.addAll(dotoos)
            } ?: kotlin.run {
                //m Toast.makeText(context,state.error?:"",Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = getCardColor())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = project.project.name,
                        modifier = Modifier
                            .padding(5.dp)
                            .weight(1f),
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        fontSize = if (project.project.name.count() < 8) {
                            38.sp
                        } else 24.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    FilledIconButton(
                        onClick = {
                            navigator?.navigate(
                                createDoTooViewDestination(
                                    project = project
                                )
                            )
                        },
                        modifier = Modifier
                            .height(40.dp)
                            .width(40.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = getOnCardColor()
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add dotoo item button",
                            tint = getOppositeOnCardColor()
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(durationMillis = 200))
                        .height(
                            if (lazyListState.isScrolled) {
                                0.dp
                            } else {
                                100.dp
                            }
                        )
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(
                        text = project.project.description,
                        color = getTextColor(),
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        fontSize = 16.sp,
                        maxLines = 2,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    ProjectHelperCard(
                        project = project
                    )
                }
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .weight(1f)


                ) {
                    androidx.compose.material.TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier
                            .padding(0.dp)
                            .background(color = MaterialTheme.colorScheme.background)
                            .height(40.dp)
                            .clip(shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)),
                        indicator = {
                            tabPositions -> TabRowDefaults.PrimaryIndicator(
                                modifier = Modifier
                                    .pagerTabIndicatorOffset(
                                        pagerState,
                                        tabPositions
                                    )
                                    .width(0.dp)
                            )
                        }
                    ) {
                        tabs.forEachIndexed{index, doTooPriorityTab ->
                            val color = remember{
                                Animatable(cardColor)
                            }
                            LaunchedEffect(key1 = pagerState.currentPage == index){
                                color.animateTo(
                                    if(pagerState.currentPage == index)
                                    {
                                        if(isDark) {
                                            Color.Black
                                        }else{
                                            Color.White
                                        }
                                    }else {
                                        cardColor
                                    }
                                )
                            }
                            Tab(
                                text ={
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            doTooPriorityTab.icon,
                                            contentDescription = "tab icon",
                                            tint = getTextColor()
                                        )
                                        Text(
                                            doTooPriorityTab.title,
                                            color = getTextColor(),
                                            fontFamily = FontFamily(Nunito.Bold.font),
                                            fontSize = 14.sp
                                        )
                                    }
                                },
                                selected = pagerState.currentPage == index,
                                modifier = Modifier
                                    .background(
                                        color = color.value
                                    ),
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                }
                            )
                        }
                    }
                    HorizontalPager(
                        count = tabs.size,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        when(tabs[pagerState.currentPage]){
                            DoTooPriorityTab.All -> {
                                DoToosLazyColumn(
                                    viewModel = viewModel,
                                    modifier = Modifier,
                                    navigator = navigator,
                                    lazyListState = lazyListState,
                                    dotoos = doToosStateList
                                )
                            }
                            DoTooPriorityTab.Today -> {
                                DoToosLazyColumn(
                                    viewModel = viewModel,
                                    modifier = Modifier,
                                    navigator = navigator,
                                    lazyListState = lazyListState,
                                    dotoos = doToosStateList.getTodayDoToo()
                                )
                            }
                            DoTooPriorityTab.Done -> {
                                DoToosLazyColumn(
                                    viewModel = viewModel,
                                    modifier = Modifier,
                                    navigator = navigator,
                                    lazyListState = lazyListState,
                                    dotoos = doToosStateList.filter { doToo -> doToo.doToo.done}
                                )
                            }
                        }

                    }
                }

            }
        }
    )

}

@Composable
fun DoToosLazyColumn(
    viewModel: DoToosViewModel,
    modifier: Modifier,
    navigator: DestinationsNavigator?,
    lazyListState: LazyListState,
    dotoos : List<DoTooWithProfiles>
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    LazyColumn(
        modifier = modifier
            .padding(10.dp)
            .fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Top)
    ) {
        val highPriorityTasks =
            dotoos.filter { dotoo -> dotoo.doToo.priority == Priorities.HIGH.toString }
        val mediumPriorityTasks =
            dotoos.filter { dotoo -> dotoo.doToo.priority == Priorities.MEDIUM.toString }
        val lowPriorityTasks =
            dotoos.filter { dotoo -> dotoo.doToo.priority == Priorities.LOW.toString }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        if (highPriorityTasks.isNotEmpty()) {
            item {
                Text(
                    text = "High Priority",
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 18.sp,
                    color = getOppositeOnCardColor()
                )
            }
            items(highPriorityTasks) { dotoo ->
                DoTooCardView(
                    doToo = dotoo,
                    onTapChat = {
                        navigator?.navigate(
                            ChatViewDestination(
                                doToo = dotoo
                            )
                        )
                    }
                ) {
                    if (dotoo.doToo.done.not()) {
                        playWooshSound(context)
                    }
                    addHapticFeedback(hapticFeedback = hapticFeedback)
                    viewModel.toggleIsDone(
                        doToo = dotoo
                    )
                }
            }
        }
        if (mediumPriorityTasks.isNotEmpty()) {
            item {
                Text(
                    text = "Not too urgent",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 18.sp,
                    color = getOppositeOnCardColor()
                )
            }
            items(mediumPriorityTasks) { dotoo ->
                DoTooCardView(
                    doToo = dotoo,
                    onTapChat = {
                        navigator?.navigate(
                            ChatViewDestination(
                                doToo = dotoo
                            )
                        )
                    },
                    onToggleDone = {
                        if (dotoo.doToo.done.not()) {
                            playWooshSound(context)
                        }
                        addHapticFeedback(hapticFeedback = hapticFeedback)
                        viewModel.toggleIsDone(
                            doToo = dotoo
                        )
                    }
                )
            }
        }
        if (lowPriorityTasks.isNotEmpty()) {
            item {
                Text(
                    text = "Do these eventually",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 18.sp,
                    color = getOppositeOnCardColor()
                )
            }
            items(lowPriorityTasks) { dotoo ->
                DoTooCardView(
                    doToo = dotoo,
                    onTapChat = {
                        navigator?.navigate(
                            ChatViewDestination(
                                doToo = dotoo
                            )
                        )
                    },
                    onToggleDone = {
                        if (dotoo.doToo.done.not()) {
                            playWooshSound(context)
                        }
                        addHapticFeedback(hapticFeedback = hapticFeedback)
                        viewModel.toggleIsDone(
                            doToo = dotoo
                        )
                    }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DotooViewPreview(){
    DoTooView(navigator = null,
        project = ProjectWithProfiles(
            project = Project(
                id = "74D46CEC-04C8-4E7E-BA2E-B9C7E8D2E958",
                name = "Test is the name",
                description = "Android is my game. Because test is my name",
                ownerId = "",
                collaboratorIds = listOf(
                    "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    "NuZXwLl3a8O3mXRcXFsJzHQgB172"
                ),
                viewerIds = listOf()
            ),
            profiles = listOf()
        )
    )
}