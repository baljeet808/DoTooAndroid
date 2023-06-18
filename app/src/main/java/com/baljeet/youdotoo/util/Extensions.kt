package com.baljeet.youdotoo.util

import androidx.compose.foundation.lazy.LazyListState
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.models.User
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

fun Long.toNiceDateTimeFormat(): String {
    return Instant.ofEpochSecond(this).toKotlinInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault()).toNiceDateTimeFormat()
}

fun LocalDateTime.toNiceDateTimeFormat(): String {
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

        if(givenDateTIme.isEqual(currentDate)){
            dateString = dateString.plus("Today ")
        }else if(givenDateTIme.isEqual(currentDate.minusDays(1))){
            dateString = dateString.plus("Yesterday ")
        }else if(givenDateTIme.isEqual(currentDate.plusDays(1))){
            dateString = dateString.plus("Tomorrow ")
        }else{
            dateString = dateString.plus(dayOfMonth.toString()).plus(" ")
                .plus(month.name.plus(", "))
                .plus(year.toString().plus("   "))
        }

        dateString = dateString.plus(hours.toString().plus(" : "))
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

val LazyListState.isScrolled: Boolean
    get() = firstVisibleItemIndex > 2 || firstVisibleItemScrollOffset > 0

fun List<DoTooWithProfiles>.getTodayDoToo(): List<DoTooWithProfiles> {
    return this.filter { dotoo ->
        Instant.ofEpochSecond(dotoo.doToo.dueDate).toKotlinInstant().toLocalDateTime(
            TimeZone.currentSystemDefault()
        ).toJavaLocalDateTime().toLocalDate().isEqual(LocalDate.now())
    }
}



