package com.baljeet.youdotoo.data.mappers

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.models.Project


fun Project.toProjectEntity() : ProjectEntity{
    return ProjectEntity(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        update = update
    )
}


fun ProjectEntity.toProject(): Project{
    return Project(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        collaboratorIds = arrayListOf(),
        viewerIds = arrayListOf(),
        update = update
    )
}