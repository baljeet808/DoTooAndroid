package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.baljeet.youdotoo.common.EnumNotificationType

@Entity(
    tableName = "notifications",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class NotificationEntity(
    @PrimaryKey
    val id : String,
    val title : String,
    val contentText : String,
    val invitationId : String?,
    val projectId : String?,
    val taskId : String?,
    val messageId : String?,
    val createdAt : Long,
    var projectColor : Long,
    val isNew : Boolean,
    val notificationType : EnumNotificationType
)