package com.baljeet.youdotoo.util

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.models.User
import com.baljeet.youdotoo.ui.theme.DotooOrange
import com.baljeet.youdotoo.ui.theme.WorkDebtPriorityColor
import kotlinx.datetime.*
import java.time.Instant
import java.time.LocalDate


fun User.asHashMap(): HashMap<String,Any>{
    return hashMapOf(
        "userId" to this.id,
        "name" to this.name,
        "email" to this.email,
        "joined" to this.joined
    )
}

fun LocalDateTime.toNiceDateTimeFormat():AnnotatedString{
    return buildAnnotatedString {
        this@toNiceDateTimeFormat.let { dateTime ->
            val dayOfMonth = dateTime.dayOfMonth
            val month = dateTime.month.name
            val year = dateTime.year
            val hour = if(dateTime.hour > 12) { dateTime.hour - 12} else dateTime.hour
            val minutes = dateTime.minute
            val isAM = dateTime.hour <12

            val pinkSpanStyle = SpanStyle(
                color = WorkDebtPriorityColor
            )
            val orangeSpanStyle = SpanStyle(
                color = DotooOrange,
            )

            withStyle(style = orangeSpanStyle){
                append(dayOfMonth.toString().plus(" "))
            }
            withStyle(style = orangeSpanStyle){
                append(month.plus(", "))
            }
            withStyle(style = orangeSpanStyle){
                append(year.toString().plus("   "))
            }
            withStyle(style = pinkSpanStyle){
                append(hour.toString().plus(" : "))
            }
            withStyle(style = pinkSpanStyle){
                append(minutes.toString())
            }
            withStyle(style = pinkSpanStyle){
                append(
                    if(isAM){
                        "AM"
                    }else{
                        "PM"
                    }
                )
            }
        }
    }
}

val LazyListState.isScrolled : Boolean
get() = firstVisibleItemIndex>2 || firstVisibleItemScrollOffset > 0

fun List<DoTooWithProfiles>.getTodayDoToo(): List<DoTooWithProfiles>{
    return this.filter {
            dotoo -> Instant.ofEpochSecond(dotoo.doToo.dueDate).toKotlinInstant().toLocalDateTime(
        TimeZone.currentSystemDefault()).toJavaLocalDateTime().toLocalDate().isEqual(LocalDate.now())
    }
}