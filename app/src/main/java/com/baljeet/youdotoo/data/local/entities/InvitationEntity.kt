package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "invitations",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InvitationEntity(
    @PrimaryKey
    val id : String,
    val inviteeId : String,
    val inviteeName : String,
    val invitedEmail : String,
    val projectId : String,
    val projectName : String,
    val projectDetail : String,
    val projectColor : Long,
    val status : Int,
    val accessType : Int
)