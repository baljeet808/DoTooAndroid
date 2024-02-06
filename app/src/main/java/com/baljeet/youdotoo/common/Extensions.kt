package com.baljeet.youdotoo.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.Interaction
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.models.UserInvitation
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toLocalDateTime
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

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this).toKotlinInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toNiceDateTimeFormat(onlyShowTime: Boolean = false): String {
    return this.toLocalDateTime().toNiceDateTimeFormat(onlyShowTime)
}

fun LocalDateTime.toNiceDateTimeFormat(onlyShowTime: Boolean = false): String {
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

        if (onlyShowTime.not()) {
            dateString = if (givenDateTIme.isEqual(currentDate)) {
                dateString.plus("Today ")
            } else if (givenDateTIme.isEqual(currentDate.minusDays(1))) {
                dateString.plus("Yesterday ")
            } else if (givenDateTIme.isEqual(currentDate.plusDays(1))) {
                dateString.plus("Tomorrow ")
            } else {
                dateString.plus(dayOfMonth.toString()).plus(" ")
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

fun LocalDate.toNiceDateFormat(showYear: Boolean = true): String {
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

        if (showYear) {
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


fun Project.getUserIds(): List<String> {
    val ids = if (this.collaboratorIds.isNotEmpty()) {
        this.collaboratorIds.filter { id -> id.isNotBlank() }.toCollection(ArrayList())
    } else {
        arrayListOf()
    }
    if (this.viewerIds.isNotEmpty()) {
        ids.addAll(this.viewerIds.filter { id -> id.isNotBlank() })
    }
    ids.add(SharedPref.userId!!)
    if (ids.none { id -> id == this.ownerId }) {
        ids.add(this.ownerId)
    }
    return ids
}


/**
 *This function will return
 * - user and his/her invitation if they have one else it will be null
 * - invitation and its user if they have one in local db else it will be null
 * - all of above will be filtered by email using search query before return, if search query is empty then return all with out filtering
 * **/
fun getUsersInvitations(
    searchQuery: String,
    users: List<UserEntity>,
    invitations: List<InvitationEntity>
): List<UserInvitation> {
    val resultingList = arrayListOf<UserInvitation>()
    users.forEach { user ->
        resultingList.add(
            UserInvitation(
                user = user,
                invitationEntity = invitations.firstOrNull { invite -> invite.invitedEmail == user.email }
            )
        )
    }
    invitations.filter { invite -> resultingList.none { r -> r.invitationEntity?.id == invite.id } }
        .forEach { invitation ->
            resultingList.add(
                UserInvitation(
                    invitationEntity = invitation,
                    user = users.firstOrNull { user -> user.email == invitation.invitedEmail }
                )
            )
        }

    val filteredList = resultingList.filter { invitation ->
        (invitation.user?.email?.startsWith(searchQuery, ignoreCase = true) == true)
                || (invitation.invitationEntity?.invitedEmail?.startsWith(
            searchQuery,
            ignoreCase = true
        ) == true)
    }

    return if (searchQuery.isNotBlank()) filteredList else resultingList
}


fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also {
        startActivity(it)
    }
}


fun String.getInteractions(): ArrayList<Interaction> {
    val interactions = arrayListOf<Interaction>()
    this.split(" | ").forEach { interactionString ->
        val interactionArray = interactionString.split(",")
        interactions.add(
            Interaction(
                userId = interactionArray[0],
                emoticonName = interactionArray[1]
            )
        )
    }
    return interactions
}

fun MessageEntity.updateInteraction(interactionName: String) {
    val interactionId = SharedPref.userId.plus(",").plus(interactionName)
    val interactions = if (this.interactions.isNotBlank()) {
        this.interactions.split(" | ").toCollection(ArrayList())
    } else {
        arrayListOf()
    }
    if (interactions.any { i -> i == interactionId }) {
        interactions.remove(interactionId)
    } else {
        interactions.add(interactionId)
    }
    this.interactions = interactions.joinToString(" | ")
}


fun Long.getDueDateEnumEntry() : DueDates{
    var currentDate = LocalDate.now().toKotlinLocalDate()
    val taskDueDate = this.toLocalDateTime().toJavaLocalDateTime().toLocalDate().toKotlinLocalDate()

    return if(currentDate == taskDueDate){
        DueDates.TODAY
    }else if(currentDate.plus(1, DateTimeUnit.DAY) == taskDueDate){
        DueDates.TOMORROW
    }else{

        // Calculating next friday date
        val currentDayOfWeek = currentDate.dayOfWeek.isoDayNumber
        currentDate = if (currentDayOfWeek == 5) {
            currentDate.plus(7, DateTimeUnit.DAY)
        } else if (currentDayOfWeek < 5) {
            currentDate.plus((5 - currentDayOfWeek), DateTimeUnit.DAY)
        } else {
            currentDate.plus(7 - (currentDayOfWeek - 5), DateTimeUnit.DAY)
        }

        if(currentDate == taskDueDate){
            DueDates.NEXT_FRIDAY
        }else{
            DueDates.CUSTOM
        }

    }
}

fun buildMoneyAnnotatedString(textColor : Color, money : String, size : TextUnit): AnnotatedString {
    return buildAnnotatedString {
        val mainText = ""

        val dollarStyle = SpanStyle(
            color = DotooBlue,
            fontSize = 14.sp
        )

        val moneyStyle = SpanStyle(
            color = DotooBlue,
            fontSize = size
        )

        val periodStyle = SpanStyle(
            color = textColor,
            fontSize = 14.sp
        )

        append(mainText)

        withStyle(style = dollarStyle) {
            append("$ ")
        }

        withStyle(style = moneyStyle){
            append(money)
        }

        withStyle(style = periodStyle) {
            append(" /mo")
        }
    }
}

//enum class EnumProjectColors(val value : Long){
//    Green(),
//    Orange(4294935846),
//    Lime(4281178457),
//    Red(4294261839),
//    //Cyan(4287356926),
//    Yellow(4294935846),
//    Purple(4290677246),
//    Graphite(4281347373),
//    Peach(4293428895),
//    //Teal(4289130720),
//    Brown(4289014314),
//    Pink(4294928820)
//}



fun String.getColor(): Color {
    return when(this){
        EnumProjectColors.Green.name -> Color(0xFF006261)
        EnumProjectColors.Pink.name -> Color(0xFFFF69B4)
        EnumProjectColors.Blue.name -> Color(0xff363CB5)
        EnumProjectColors.Red.name -> Color(0xFFF53C4F)
        EnumProjectColors.Yellow.name -> Color(0xFFFF8526)
        EnumProjectColors.Brown.name -> Color(0xFFA52A2A)
        EnumProjectColors.Black.name -> Color(0xFF302D2D)
        EnumProjectColors.Cyan.name -> Color(0xFF8BDFFE)
        EnumProjectColors.Indigo.name -> Color(0xFF4b0082)
        else -> {Color(0xFF4b0082)}
    }
}