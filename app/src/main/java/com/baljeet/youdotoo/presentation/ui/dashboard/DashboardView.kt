package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.common.DestinationAccountRoute
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.menuItems
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.presentation.ui.attachment_viewer.addAttachmentViewerDestination
import com.baljeet.youdotoo.presentation.ui.chat.addChatViewDestination
import com.baljeet.youdotoo.presentation.ui.create_task.addCreateTaskViewDestination
import com.baljeet.youdotoo.presentation.ui.createproject.addCreateProjectViewDestination
import com.baljeet.youdotoo.presentation.ui.drawer.NavigationDrawer
import com.baljeet.youdotoo.presentation.ui.drawer.components.TopBar
import com.baljeet.youdotoo.presentation.ui.edittask.addEditTaskViewDestination
import com.baljeet.youdotoo.presentation.ui.invitation.addInvitationViewDestination
import com.baljeet.youdotoo.presentation.ui.profile_quick_view.addProfileQuickViewDestination
import com.baljeet.youdotoo.presentation.ui.project.addProjectViewDestination
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectsRoute
import com.baljeet.youdotoo.presentation.ui.projects.addProjectsViewDestination
import com.baljeet.youdotoo.presentation.ui.settings.DestinationSettingsRoute
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightDarkColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun DashboardView(
    allTasks: List<DoTooItemEntity>,
    userData : UserData,
    logout : () -> Unit,
    onClickNotifications: () -> Unit,
    onClickSettings : () -> Unit,
    openProfile : () -> Unit,
    navigateToProjectsOnlyView : () -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val darkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    var maximizeCurrentScreen by remember {
        mutableStateOf(true)
    }

    val statusBarColor by animateColorAsState(
        if (maximizeCurrentScreen) {
            getLightThemeColor()
        } else {
            getDarkThemeColor()
        }, label = ""
    )

    systemUiController.setSystemBarsColor(color = statusBarColor)

    val scale = animateFloatAsState(
        if (maximizeCurrentScreen) {
            1f
        } else {
            0.8f
        }, label = ""
    )

    val roundness = animateDpAsState(
        if (maximizeCurrentScreen) {
            0.dp
        } else {
            40.dp
        }, label = ""
    )

    val offSetX = animateDpAsState(
        if (maximizeCurrentScreen) {
            0.dp
        } else {
            250.dp
        }, label = ""
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
                    userData = userData,
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
                                onClickSettings()
                            }
                            DestinationProjectsRoute -> {
                                scope.launch {
                                    scaffoldState.drawerState.close()
                                }
                                maximizeCurrentScreen = true
                                navigateToProjectsOnlyView()
                            }
                        }
                    },
                    closeDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        maximizeCurrentScreen = true
                    },
                    logout = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                        maximizeCurrentScreen = true
                        logout()
                    },
                    modifier = Modifier.width(250.dp),
                    allTasks = allTasks,
                    openProfile = {
                        scope.launch {
                            scaffoldState.drawerState.close()
                            maximizeCurrentScreen = true
                            delay(500)
                            openProfile()
                        }
                    }
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
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (darkTheme) {
                        getNightDarkColor()
                    } else {
                        getNightLightColor()
                    }
                )
                .padding(padding)
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
                        color = getLightThemeColor(),
                        shape = RoundedCornerShape(roundness.value)
                    )
                    .clip(shape = RoundedCornerShape(roundness.value)),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {


                /**
                 * Top app bar
                 * **/
                AnimatedVisibility(visible = navBackStackEntry?.destination?.route == DestinationProjectsRoute) {
                    TopBar(
                        modifier = Modifier.height(60.dp),
                        notificationsState = true,
                        onMenuItemClick = {
                            scope.launch {
                                scaffoldState.drawerState.open()
                            }
                            maximizeCurrentScreen = false
                        },
                        onNotificationsClicked = onClickNotifications
                    )
                }



                NavHost(
                    navController = navController,
                    startDestination = DestinationProjectsRoute,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    addProjectsViewDestination(navController)
                    addProjectViewDestination(navController)
                    addCreateTaskViewDestination(navController)
                    addCreateProjectViewDestination(navController)
                    addChatViewDestination(navController)
                    addInvitationViewDestination(navController)
                    addEditTaskViewDestination(navController)
                    addAttachmentViewerDestination(navController)
                    addProfileQuickViewDestination(navController)
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
        ),
        userData = UserData(
            userEmail = "",
            userId = "",
            userName = "",
            profilePictureUrl = ""
        ),
        onClickNotifications = {},
        logout = {},
        onClickSettings = {},
        openProfile = {},
        navigateToProjectsOnlyView = {}
    )
}