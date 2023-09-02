package com.baljeet.youdotoo.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity

data class TaskWithProject(

    @Embedded
    val task : TaskEntity,

    @Relation(
        parentColumn = "projectId",
        entityColumn = "id",
        entity = ProjectEntity::class
    )
    var projectEntity : ProjectEntity

)
