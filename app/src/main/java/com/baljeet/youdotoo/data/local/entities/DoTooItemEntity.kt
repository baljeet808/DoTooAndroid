package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class DoTooItemEntity(
    @PrimaryKey
    val id : String,
    val title : String,
    val projectId : String,
    val description : String,
    val dueDate : Long,
    val createDate : Long,
    var done : Boolean,
    val priority : String,
    var updatedBy : String,
    var projectColor : Long
)