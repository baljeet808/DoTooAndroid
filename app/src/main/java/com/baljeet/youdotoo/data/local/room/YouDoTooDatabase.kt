package com.baljeet.youdotoo.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.baljeet.youdotoo.data.local.converters.NotificationTypeConverter
import com.baljeet.youdotoo.data.local.daos.ColorPaletteDao
import com.baljeet.youdotoo.data.local.daos.DoTooItemDao
import com.baljeet.youdotoo.data.local.daos.InvitationDao
import com.baljeet.youdotoo.data.local.daos.MessageDao
import com.baljeet.youdotoo.data.local.daos.NotificationDao
import com.baljeet.youdotoo.data.local.daos.ProjectDao
import com.baljeet.youdotoo.data.local.daos.UserDao
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity


@Database(
    entities = [
        ProjectEntity::class,
        DoTooItemEntity::class,
        UserEntity::class,
        InvitationEntity::class,
        NotificationEntity::class,
        ColorPaletteEntity::class,
        MessageEntity::class
    ],
    version = 21
)
@TypeConverters(NotificationTypeConverter::class)
abstract class YouDoTooDatabase  : RoomDatabase(){
    abstract val projectDao : ProjectDao
    abstract val userDao : UserDao
    abstract val doTooItemDao : DoTooItemDao
    abstract val invitationDao : InvitationDao
    abstract val notificationDao : NotificationDao
    abstract val colorPaletteDao : ColorPaletteDao
    abstract val messageDao : MessageDao
}