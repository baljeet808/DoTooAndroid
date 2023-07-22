package com.baljeet.youdotoo.presentation.ui.dashboard.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.presentation.ui.chat.addChatViewDestination
import com.baljeet.youdotoo.presentation.ui.create_task.addCreateTaskViewDestination
import com.baljeet.youdotoo.presentation.ui.drawer.NavigationDrawer
import com.baljeet.youdotoo.presentation.ui.drawer.components.TopBar
import com.baljeet.youdotoo.presentation.ui.project.addProjectViewDestination
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectsRoute
import com.baljeet.youdotoo.presentation.ui.projects.addProjectsViewDestination
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun DashboardView(
    allTasks: List<DoTooItemEntity>
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val darkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    var maximizeCurrentScreen by remember {
        mutableStateOf(true)
    }

    val statusBarColor by animateColorAsState(
        if (maximizeCurrentScreen) {
            if (isSystemInDarkTheme()) {
                NightDotooNormalBlue
            } else {
                DotooGray
            }
        } else {
            if (darkTheme) {
                NightDotooDarkBlue
            } else {
                NightDotooNormalBlue
            }
        }
    )

    systemUiController.setSystemBarsColor(color = statusBarColor)

    val scale = animateFloatAsState(
        if (maximizeCurrentScreen) {
            1f
        } else {
            0.8f
        }
    )

    val roundness = animateDpAsState(
        if (maximizeCurrentScreen) {
            0.dp
        } else {
            40.dp
        }
    )

    val offSetX = animateDpAsState(
        if (maximizeCurrentScreen) {
            0.dp
        } else {
            250.dp
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        drawerScrimColor = Color.Transparent,
        drawerGesturesEnabled = false,
        drawerBackgroundColor = Color.Transparent,
        drawerElevation = 0.dp,
        drawerContent = {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                NavigationDrawer(
                    userData = UserData(
                        userId = SharedPref.userId ?: "",
                        userName = SharedPref.userName,
                        userEmail = SharedPref.userEmail,
                        profilePictureUrl = SharedPref.userAvatar
                    ),
                    menuItems = menuItems,
                    onMenuItemClick = { menuId ->
                        when (menuId.id) {
                            DestinationAccountRoute -> {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                maximizeCurrentScreen = true
                            }
                            DestinationSettingsRoute -> {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                maximizeCurrentScreen = true
                            }
                            DestinationProjectsRoute -> {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                maximizeCurrentScreen = true
                                navController.navigate(DestinationProjectsRoute)
                            }
                        }
                    },
                    closeDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        maximizeCurrentScreen = true
                    },
                    modifier = Modifier.width(250.dp),
                    allTasks = allTasks
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent)
                        .clickable(
                            onClick = {
                                if (scaffoldState.drawerState.isOpen) {
                                    scope.launch {
                                        scaffoldState.drawerState.close()
                                    }
                                    maximizeCurrentScreen = true
                                }
                            }
                        )
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (darkTheme) {
                        NightDotooDarkBlue
                    } else {
                        NightDotooNormalBlue
                    }
                )
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        x = offSetX.value,
                        y = 0.dp
                    )
                    .scale(scale.value)
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            NightDotooNormalBlue
                        } else {
                            DotooGray
                        },
                        shape = RoundedCornerShape(roundness.value)
                    )
                    .clip(shape = RoundedCornerShape(roundness.value)),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {

                /**
                 * Top app bar
                 * **/
                TopBar(
                    modifier = Modifier.height(60.dp),
                    notificationsState = true,
                    onMenuItemClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                        maximizeCurrentScreen = false
                    },
                    onNotificationItemClicked = {
                        //Navigate to notifications
                    }
                )


                NavHost(
                    navController = navController,
                    startDestination = DestinationProjectsRoute,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    addProjectsViewDestination(navController)
                    addProjectViewDestination(navController)
                    addCreateTaskViewDestination(navController)
                    addChatViewDestination()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDashboardView() {
    DashboardView(
        allTasks = listOf(
            getSampleDotooItem().toDoTooItemEntity("")
        )
    )
}