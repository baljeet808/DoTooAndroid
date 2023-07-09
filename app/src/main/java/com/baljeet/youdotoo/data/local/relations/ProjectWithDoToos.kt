package com.baljeet.youdotoo.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity

data class ProjectWithDoToos(

    @Embedded
    val project : ProjectEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "projectId",
        entity = DoTooItemEntity::class
    )
    var tasks : List<DoTooItemEntity> = listOf()

)
