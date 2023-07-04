package com.baljeet.youdotoo.data.mappers

import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.models.DoTooItem


fun DoTooItem.toDoTooItemEntity(projectId: String) : DoTooItemEntity{
    return DoTooItemEntity(
        id = id,
        title = title,
        description = description,
        projectId = projectId,
        dueDate = dueDate,
        createDate = createDate,
        done = done,
        priority = priority,
        updatedBy = updatedBy
    )
}


fun DoTooItemEntity.toDoTooItem(): DoTooItem{
    return DoTooItem(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        createDate = createDate,
        done = done,
        priority = priority,
        updatedBy = updatedBy
    )
}