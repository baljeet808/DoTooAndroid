package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.models.Project


enum class EnumRoles {
    ProAdmin, Admin, Editor, Viewer, Blocked
}


fun getRole(project: Project): EnumRoles {
    return if (project.ownerId == SharedPref.userId) {
        if (SharedPref.isUserAPro) {
            EnumRoles.ProAdmin
        } else {
            EnumRoles.Admin
        }
    } else if (project.collaboratorIds.contains(SharedPref.userId)) {
        EnumRoles.Editor
    } else if (project.viewerIds.contains(SharedPref.userId)) {
        EnumRoles.Viewer
    } else {
        EnumRoles.Blocked
    }
}


fun doesUserHavePermissionToEdit(project: ProjectEntity) =
    (project.ownerId == SharedPref.userId!!) || project.collaboratorIds.contains(SharedPref.userId!!)

