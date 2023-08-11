package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    var id: String,
    var senderId: String,
    var message: String,
    var createdAt: Long,
    var isUpdate: Boolean,
    var attachmentUrl: String?,
    var interactions: String,
    var projectId: String
)