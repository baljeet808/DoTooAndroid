package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    var id : String,
    var name : String,
    var email :String,
    var joined : Long,
    var avatarUrl: String = "",
    var firebaseToken : String = ""
)