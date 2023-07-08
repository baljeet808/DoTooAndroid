package com.baljeet.youdotoo.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Flaky
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.*

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
        val currentDate = java.time.LocalDate.now().toKotlinLocalDate()

        return when(this){
            TODAY -> {
                currentDate
            }
            TOMORROW -> {
                currentDate.plus(1,DateTimeUnit.DAY)
            }
            NEXT_FRIDAY -> {
                val currentDayOfWeek = currentDate.dayOfWeek.isoDayNumber
                if(currentDayOfWeek==5){
                    currentDate.plus(7,DateTimeUnit.DAY)
                }else if(currentDayOfWeek < 5){
                    currentDate.plus((5-currentDayOfWeek),DateTimeUnit.DAY)
                }else{
                    currentDate.plus(7 - (currentDayOfWeek - 5), DateTimeUnit.DAY)
                }
                currentDate
            }
            else -> {
                currentDate
            }
        }
    }

    fun getExactDateTimeInSecondsFrom1970(): Long {
        val date = getExactDate()
        return LocalDateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hour = 9,
            minute = 0,
            second = 0
        ).toInstant(TimeZone.currentSystemDefault()).epochSeconds
    }
}

sealed class DoTooPriorityTab( var icon : ImageVector, var title: String){
    object All : DoTooPriorityTab(
        icon = Icons.Default.Flaky,
        title = "All"
    )
    object Today : DoTooPriorityTab(
        icon = Icons.Default.Today,
        title = "Today"
    )
    object Done : DoTooPriorityTab(
        icon = Icons.Default.Checklist,
        title = "Done"
    )
}


enum class BottomSheetType() {
    TYPE1, TYPE2
}


enum class ChatScreenBottomSheetTypes() {
    MESSAGE_EMOTICONS, CUSTOM_EMOTICONS, PERSON_TAGGER, COLLABORATOR_SCREEN
}