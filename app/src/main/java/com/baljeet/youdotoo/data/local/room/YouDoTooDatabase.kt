package com.baljeet.youdotoo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.baljeet.youdotoo.data.local.daos.DoTooItemDao
import com.baljeet.youdotoo.data.local.daos.InvitationDao
import com.baljeet.youdotoo.data.local.daos.ProjectDao
import com.baljeet.youdotoo.data.local.daos.UserDao
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity


@Database(
    entities = [
        ProjectEntity::class,
        DoTooItemEntity::class,
        UserEntity::class,
        InvitationEntity::class
    ],
    version = 9
)
abstract class YouDoTooDatabase  : RoomDatabase(){
    abstract val projectDao : ProjectDao
    abstract val userDao : UserDao
    abstract val doTooItemDao : DoTooItemDao
    abstract val invitationDao : InvitationDao
}