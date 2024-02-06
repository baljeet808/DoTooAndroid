package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinLocalDate

enum class EnumPriorities(val toString: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
}

enum class DueDates(val toString : String) {
    NEXT_FRIDAY("Next Friday"),
    TOMORROW("Tomorrow"),
    TODAY("Today"),
    CUSTOM("Custom");

    fun getExactDate(): LocalDate {
        var currentDate = java.time.LocalDate.now().toKotlinLocalDate()

        return when (this) {
            TODAY -> {
                currentDate
            }
            TOMORROW -> {
                currentDate.plus(1, DateTimeUnit.DAY)
            }
            NEXT_FRIDAY -> {
                val currentDayOfWeek = currentDate.dayOfWeek.isoDayNumber
                currentDate = if (currentDayOfWeek == 5) {
                    currentDate.plus(7, DateTimeUnit.DAY)
                } else if (currentDayOfWeek < 5) {
                    currentDate.plus((5 - currentDayOfWeek), DateTimeUnit.DAY)
                } else {
                    currentDate.plus(7 - (currentDayOfWeek - 5), DateTimeUnit.DAY)
                }
                currentDate
            }
            else -> {
                currentDate
            }
        }
    }
}




enum class EnumCreateTaskSheetType {
    SELECT_PROJECT, SELECT_DUE_DATE, SELECT_PRIORITY
}

enum class EnumNotificationType{
    NewMessage, NewInvitation, ProjectUpdate, TaskUpdate, AccessUpdate, InvitationUpdate, MessageUpdate, General
}


enum class ChatScreenBottomSheetTypes() {
    MESSAGE_EMOTICONS, CUSTOM_EMOTICONS, PERSON_TAGGER, COLLABORATOR_SCREEN
}


enum class EnumProjectColors{
    Green,
    Pink,
    Blue,
    Red,
    Yellow,
    Brown,
    Black,
    Cyan,
    Indigo
}

enum class EnumDashboardTasksTabs{
    Today,Tomorrow,Yesterday,Pending,AllOther
}

data class DashboardTaskTabs (
    var name: EnumDashboardTasksTabs = EnumDashboardTasksTabs.Today,
    var taskCount: Int = 0,
    var index: Int = 0,
    var tasks : List<TaskWithProject> = listOf()
    )
data class DashboardTaskTabsByPriorities (
    var name: EnumPriorities = EnumPriorities.HIGH,
    var taskCount: Int = 0,
    var index: Int = 0,
    var tasks : List<TaskWithProject> = listOf()
    )
