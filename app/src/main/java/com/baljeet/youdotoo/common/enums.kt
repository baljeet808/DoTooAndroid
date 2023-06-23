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
    UPCOMING_FRIDAY_5PM("Upcoming Friday 5 PM"),
    TOMORROW_5PM("Tomorrow 5 PM"),
    TOMORROW_9AM("Tomorrow 9 AM"),
    TONIGHT_9PM("Tonight 9 PM"),
    TODAY_5PM("Today 5 PM"),
    INDEFINITE("Indefinite"),
    CUSTOM("Custom");

    fun getExactDateTime(): LocalDateTime {
        val currentDateTime = java.time.LocalDateTime.now().toKotlinLocalDateTime()

        return when(this){
            TODAY_5PM -> {
                LocalDateTime(
                    year = currentDateTime.year,
                    month = currentDateTime.month,
                    dayOfMonth = currentDateTime.dayOfMonth,
                    hour = 17,
                    minute = 0,
                    second = 0
                )
            }
            TONIGHT_9PM -> {
                LocalDateTime(
                    year = currentDateTime.year,
                    month = currentDateTime.month,
                    dayOfMonth = currentDateTime.dayOfMonth,
                    hour = 21,
                    minute = 0,
                    second = 0
                )
            }
            TOMORROW_9AM -> {
                LocalDateTime(
                    year = currentDateTime.year,
                    month = currentDateTime.month,
                    dayOfMonth = currentDateTime.dayOfMonth.plus(1),
                    hour = 9,
                    minute = 0,
                    second = 0
                )
            }
            TOMORROW_5PM -> {
                LocalDateTime(
                    year = currentDateTime.year,
                    month = currentDateTime.month,
                    dayOfMonth = currentDateTime.dayOfMonth.plus(1),
                    hour = 17,
                    minute = 0,
                    second = 0
                )
            }
            UPCOMING_FRIDAY_5PM -> {
                val currentDayOfWeek = currentDateTime.dayOfWeek.isoDayNumber
                val dayOfMonth = if(currentDayOfWeek < 5 ){
                    currentDateTime.dayOfMonth.plus((5 - currentDayOfWeek))
                }else{
                    currentDateTime.dayOfMonth.plus(7 - (currentDayOfWeek-5))
                }
                LocalDateTime(
                    year = currentDateTime.year,
                    month = currentDateTime.month,
                    dayOfMonth = dayOfMonth,
                    hour = 17,
                    minute = 0,
                    second = 0
                )
            }
            else -> { currentDateTime }
        }
    }

    fun getExactDateTimeInSecondsFrom1970(): Long {
        return getExactDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds
    }
}

data class ApiCallState(
    val isSuccessful : Boolean? = null,
    val error : String? = null
)

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