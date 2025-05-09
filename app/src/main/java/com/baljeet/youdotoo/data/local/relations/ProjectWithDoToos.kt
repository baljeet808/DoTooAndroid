package com.baljeet.youdotoo.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity

data class ProjectWithDoToos(

    @Embedded
    val project : ProjectEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "projectId",
        entity = TaskEntity::class
    )
    var tasks : List<TaskEntity> = listOf()

)
