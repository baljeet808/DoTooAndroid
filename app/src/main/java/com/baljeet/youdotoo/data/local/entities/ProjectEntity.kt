package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDateInLong


@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey
    var id : String = "",
    var name : String = "",
    var description : String = "",
    var ownerId : String = "",
    var update : String = "",
    var color : String = getRandomColor(),
    var collaboratorIds : String = "",
    var viewerIds: String = "",
    var updatedAt : Long = getSampleDateInLong(),
    var hideFromDashboard : Boolean = false
)