package com.baljeet.youdotoo.data.local.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.baljeet.youdotoo.common.EnumNotificationType


@ProvidedTypeConverter
class NotificationTypeConverter {
    @TypeConverter
    fun notificationTypToInt(value: EnumNotificationType): Int {
        return when (value) {
            EnumNotificationType.MESSAGE -> 0
            EnumNotificationType.TASK_UPDATE -> 1
            EnumNotificationType.INVITATION -> 2
            EnumNotificationType.PROJECT_UPDATE -> 3
            EnumNotificationType.GENERAL -> 4
        }
    }

    @TypeConverter
    fun intToNotificationType(value: Int): EnumNotificationType {
        return when (value) {
            0 -> EnumNotificationType.MESSAGE
            1 -> EnumNotificationType.TASK_UPDATE
            2 -> EnumNotificationType.INVITATION
            3 -> EnumNotificationType.PROJECT_UPDATE
            else -> EnumNotificationType.GENERAL
        }
    }
}