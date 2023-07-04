package com.baljeet.youdotoo.presentation.ui.dashboard.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.baljeet.youdotoo.TrackerObject
import com.baljeet.youdotoo.common.DestinationAccountRoute
import com.baljeet.youdotoo.common.DestinationSettingsRoute
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.menuItems
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.presentation.ui.chat.addChatViewDestination
import com.baljeet.youdotoo.presentation.ui.dotoo.addDotooViewDestination
import com.baljeet.youdotoo.presentation.ui.drawer.NavigationDrawer
import com.baljeet.youdotoo.presentation.ui.drawer.components.TopBar
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectRoute
import com.baljeet.youdotoo.presentation.ui.projects.addProjectViewDestination
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun DashboardView(
    trackerObject: TrackerObject
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val navController = rememberNavController()
    val darkTheme = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    val statusBarColor by animateColorAsState(
        if(scaffoldState.drawerState.isClosed){
            if(darkTheme) {
                NightDotooNormalBlue
            }else{
               Color.White
            }
        }else{
            if(darkTheme) {
             NightDotooDarkBlue
            }else{
                NightDotooNormalBlue
            }
        }
    )

    systemUiController.setSystemBarsColor(color = statusBarColor)




    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,

        /**
         * Side navigation drawer body content
         * **/
        drawerContent = {
            NavigationDrawer(
                userData = UserData(
                    userId = SharedPref.userId?:"",
                    userName = SharedPref.userName,
                    userEmail = SharedPref.userEmail,
                    profilePictureUrl = SharedPref.userAvatar
                ),
                menuItems = menuItems,
                onMenuItemClick = {menuId ->
                    when(menuId.id){
                        DestinationAccountRoute ->{

                        }
                        DestinationSettingsRoute ->{

                        }
                        DestinationProjectRoute -> {
                            navController.navigate(DestinationProjectRoute)
                        }
                    }
                },
                closeDrawer = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                },
                modifier = Modifier
            )
        }
    ) {

        Column(
            modifier = Modifier
                .background( color = if(darkTheme){
                    NightDotooNormalBlue
                }else{
                    Color.White
                }),
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
                },
                onNotificationItemClicked = {
                    //Navigate to notifications
                },
                onSearchItemClicked = {
                    //show search results somewhere
                }
            )

            NavHost(
                navController = navController,
                startDestination = DestinationProjectRoute,
                modifier = Modifier.fillMaxSize()
            ){
                addProjectViewDestination(navController, trackerObject = trackerObject)
                addDotooViewDestination(navController = navController, trackerObject = trackerObject)
                addChatViewDestination(trackerObject = trackerObject)
            }

        }



    }

}


@Preview(showBackground = true)
@Composable
fun PreviewDashboardView(){
    DashboardView(
        trackerObject = TrackerObject()
    )
}