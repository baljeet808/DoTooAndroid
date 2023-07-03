package com.baljeet.youdotoo.presentation.ui.dashboard.component

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.baljeet.youdotoo.common.DestinationAccountRoute
import com.baljeet.youdotoo.common.DestinationSettingsRoute
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.menuItems
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.presentation.ui.drawer.NavigationDrawer
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectRoute
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun DashboardView(
    navigateToSettings : () -> Unit,
    navigateToAccount : () -> Unit,
    navigateToAllProjects : () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        /**
         * Side navigation drawer body content
         * **/
        drawerContent = {
            NavigationDrawer(
                userData = UserData(
                    userId = SharedPref.userId!!,
                    userName = SharedPref.userName,
                    userEmail = SharedPref.userEmail,
                    profilePictureUrl = SharedPref.userAvatar
                ),
                menuItems = menuItems,
                onMenuItemClick = {menuId ->
                    when(menuId.id){
                        DestinationAccountRoute ->{
                            navigateToAccount()
                        }
                        DestinationSettingsRoute ->{
                            navigateToSettings()
                        }
                        DestinationProjectRoute -> {
                            navigateToAllProjects()
                        }
                    }
                },
                closeDrawer = {
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    ) {



    }

}