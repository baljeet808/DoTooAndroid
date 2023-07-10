package com.baljeet.youdotoo.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import com.baljeet.youdotoo.presentation.ui.drawer.MenuItem
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectsRoute

/**
 * Updated by Baljeet singh
 * **/

const val ConstFirstScreenDescription = "Work on tasks with family and friends. and finish them all together."
const val ConstSecScreenDescription = "Chat about specific task. Manage permissions for collaborators."
const val ConstThirdScreenDescription = "Create multiple DoToos and track there progress."
const val ConstSampleAvatarUrl = "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/20.png?alt=media&token=fa1489d4-8951-4ef6-8f96-862938aedb62"

const val DestinationAccountRoute = "account"
const val DestinationSettingsRoute = "settings"


const val maxTitleCharsAllowed = 60
const val maxDescriptionCharsAllowed = 120
const val maxTitleCharsAllowedForProject = 40

val menuItems = arrayListOf(
    MenuItem(
        id = DestinationAccountRoute,
        title = "Account",
        icon = Icons.Outlined.Person,
        contentDescription = "Menu item to show Account"
    ),
    MenuItem(
        id = DestinationProjectsRoute,
        title = "Projects",
        icon = Icons.Outlined.FolderOpen,
        contentDescription = "Menu item to show all Projects"
    ),
    MenuItem(
        id = DestinationSettingsRoute,
        title = "Settings",
        icon = Icons.Outlined.Settings,
        contentDescription = "Menu item to show settings"
    )
)