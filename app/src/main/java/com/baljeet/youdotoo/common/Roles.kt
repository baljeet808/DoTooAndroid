package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.domain.models.Project


enum class Roles {
    ProAdmin, Admin, Editor, Viewer, Blocked
}


fun getRole(project: Project): Roles {
    return if (project.ownerId == SharedPref.userId) {
        if (SharedPref.isUserAPro) {
            Roles.ProAdmin
        } else {
            Roles.Admin
        }
    } else if (project.collaboratorIds.contains(SharedPref.userId)) {
        Roles.Editor
    } else if (project.viewerIds.contains(SharedPref.userId)) {
        Roles.Viewer
    } else {
        Roles.Blocked
    }
}