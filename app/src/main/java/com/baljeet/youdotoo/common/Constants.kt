package com.baljeet.youdotoo.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Settings
import com.baljeet.youdotoo.domain.models.MenuItem
import com.baljeet.youdotoo.presentation.ui.projects.DestinationProjectsRoute
import com.baljeet.youdotoo.presentation.ui.settings.DestinationSettingsRoute

/**
 * Updated by Baljeet singh
 * **/

const val ConstFirstScreenDescription = "Work on tasks with family and friends. and finish them all together."
const val ConstSecScreenDescription = "Chat about specific task. Manage permissions for collaborators."
const val ConstThirdScreenDescription = "Create multiple DoToos and track there progress."
const val ConstSampleAvatarUrl = "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/20.png?alt=media&token=fa1489d4-8951-4ef6-8f96-862938aedb62"



const val maxTitleCharsAllowed = 60
const val maxDescriptionCharsAllowed = 120
const val maxTitleCharsAllowedForProject = 40



const val InvitationPending = 0
const val InvitationAccepted = 1
const val InvitationDeclined = 2
const val InvitationArchived = 3

const val AccessTypeAdmin = 0
const val AccessTypeEditor = 1
const val AccessTypeViewer = 2

val menuItems = arrayListOf(
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