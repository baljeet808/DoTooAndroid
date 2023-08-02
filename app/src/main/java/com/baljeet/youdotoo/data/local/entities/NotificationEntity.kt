package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.data.local.converters.NotificationTypeConverter

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
@TypeConverters(NotificationTypeConverter::class)
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