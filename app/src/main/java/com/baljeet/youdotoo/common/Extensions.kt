package com.baljeet.youdotoo.common

import androidx.compose.foundation.lazy.LazyListState
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import kotlinx.datetime.*
import java.time.Instant
import java.time.LocalDate


fun User.asHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "userId" to this.id,
        "name" to this.name,
        "email" to this.email,
        "joined" to this.joined
    )
}

fun Long.toNiceDateTimeFormat(onlyShowTime : Boolean = false): String {
    return Instant.ofEpochSecond(this).toKotlinInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault()).toNiceDateTimeFormat(onlyShowTime)
}

fun LocalDateTime.toNiceDateTimeFormat(onlyShowTime : Boolean = false): String {
    this.let { dateTime ->

        val hours = if (hour > 12) {
            hour - 12
        } else hour
        val minutes = if (minute < 10) {
            "0".plus(minute)
        } else minute
        val isAM = dateTime.hour < 12

        var dateString = ""

        val givenDateTIme = dateTime.toJavaLocalDateTime().toLocalDate()
        val currentDate = LocalDate.now()

        if(onlyShowTime.not()) {
            if (givenDateTIme.isEqual(currentDate)) {
                dateString = dateString.plus("Today ")
            } else if (givenDateTIme.isEqual(currentDate.minusDays(1))) {
                dateString = dateString.plus("Yesterday ")
            } else if (givenDateTIme.isEqual(currentDate.plusDays(1))) {
                dateString = dateString.plus("Tomorrow ")
            } else {
                dateString = dateString.plus(dayOfMonth.toString()).plus(" ")
                    .plus(month.name.plus(", "))
                    .plus(year.toString().plus("   "))
            }
        }

        dateString = dateString.plus(hours.toString().plus(":"))
            .plus(minutes.toString())
            .plus(
                if (isAM) {
                    " AM"
                } else {
                    " PM"
                }
            )

        return dateString
    }
}

fun LocalDate.toNiceDateFormat(showYear : Boolean = true): String {
    this.let { dateTime ->

        var dateString = ""

        val currentDate = LocalDate.now()

        dateString = if (dateTime.isEqual(currentDate)) {
            dateString.plus("Today ")
        } else if (dateTime.isEqual(currentDate.minusDays(1))) {
            dateString.plus("Yesterday ")
        } else if (dateTime.isEqual(currentDate.plusDays(1))) {
            dateString.plus("Tomorrow ")
        } else {
            dateString.plus(dayOfMonth.toString()).plus(" ")
                .plus(month.name)
        }

        if(showYear) {
            dateString = dateString.plus(", ").plus(year.toString())
        }

        return dateString
    }
}

fun LocalDate.formatNicelyWithoutYear(): String {
        return dayOfMonth.toString().plus(" ").plus(month.name).plus(", ").plus(dayOfWeek.name)
}

fun kotlinx.datetime.LocalDate.getExactDateTimeInSecondsFrom1970(): Long {
    val date = this
    return LocalDateTime(
        year = date.year,
        monthNumber = date.monthNumber,
        dayOfMonth = date.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault()).epochSeconds
}



val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 2 || firstVisibleItemScrollOffset > 0


fun Project.getUserIds(): List<String>{
    val ids = if (this.collaboratorIds.isNotEmpty()) {
        this.collaboratorIds.filter { id -> id.isNotBlank() }.toCollection(ArrayList())
    } else {
        arrayListOf()
    }
    if (this.viewerIds.isNotEmpty()) {
        ids.addAll(this.viewerIds.filter { id -> id.isNotBlank() })
    }
    ids.add(SharedPref.userId!!)
    return ids
}



