package com.baljeet.youdotoo.data.local.converters

import androidx.room.TypeConverter
import com.baljeet.youdotoo.common.EnumNotificationType


class NotificationTypeConverter {
    @TypeConverter
    fun notificationTypToInt(value: EnumNotificationType): Int {
        return when (value) {
            EnumNotificationType.NewMessage -> 0
            EnumNotificationType.NewInvitation -> 1
            EnumNotificationType.InvitationUpdate -> 2
            EnumNotificationType.AccessUpdate -> 3
            EnumNotificationType.ProjectUpdate -> 4
            EnumNotificationType.MessageUpdate -> 5
            EnumNotificationType.TaskUpdate -> 6
            EnumNotificationType.General -> 7
        }
    }

    @TypeConverter
    fun intToNotificationType(value: Int): EnumNotificationType {
        return when (value) {
            0 -> EnumNotificationType.NewMessage
            1 -> EnumNotificationType.NewInvitation
            2 -> EnumNotificationType.InvitationUpdate
            3 -> EnumNotificationType.AccessUpdate
            4 -> EnumNotificationType.ProjectUpdate
            5 -> EnumNotificationType.MessageUpdate
            6 -> EnumNotificationType.TaskUpdate
            else -> EnumNotificationType.General
        }
    }
}