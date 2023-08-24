package com.baljeet.youdotoo.common

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinLocalDate

enum class Priorities(val toString: String) {
    LOW("Low"),
    HIGH("High"),
    MEDIUM("Medium"),
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

enum class EnumProjectColors(val longValue : Long){
    Green(4278215265),
    Orange(4294935846),
    Lime(4281178457),
    Red(4294261839),
    //Cyan(4287356926),
    Yellow(4294935846),
    Purple(4290677246),
    Graphite(4281347373),
    Peach(4293428895),
    //Teal(4289130720),
    Brown(4289014314),
    Pink(4294928820)
}

enum class EnumDashboardTasksTabs{
    Today,Tomorrow,Yesterday,Pending,AllOther
}

data class DashboardTaskTabs (
    var name: EnumDashboardTasksTabs = EnumDashboardTasksTabs.Today,
    var taskCount: Int = 0,
    var index: Int = 0
    )
