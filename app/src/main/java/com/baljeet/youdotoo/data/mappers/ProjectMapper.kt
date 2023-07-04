package com.baljeet.youdotoo.data.mappers

import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.models.Project


fun Project.toProjectEntity() : ProjectEntity{
    return ProjectEntity(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        collaboratorIds = collaboratorIds.joinToString(","),
        viewerIds = viewerIds.joinToString(",")
    )
}


fun ProjectEntity.toProject(): Project{
    return Project(
        id = id,
        name = name,
        description = description,
        ownerId = ownerId,
        collaboratorIds = collaboratorIds.split(","),
        viewerIds = viewerIds.split(",")
    )
}