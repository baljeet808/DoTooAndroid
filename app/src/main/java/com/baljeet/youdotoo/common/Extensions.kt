package com.baljeet.youdotoo.common

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.lazy.LazyListState
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.models.UserInvitation
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

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochSecond(this).toKotlinInstant()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}

fun Long.toNiceDateTimeFormat(onlyShowTime : Boolean = false): String {
    return this.toLocalDateTime().toNiceDateTimeFormat(onlyShowTime)
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


/**
 *This function will return
 * - user and his/her invitation if they have one else it will be null
 * - invitation and its user if they have one in local db else it will be null
 * - all of above will be filtered by email using search query before return, if search query is empty then return all with out filtering
 * **/
fun getUsersInvitations(searchQuery : String,users : List<UserEntity>, invitations : List<InvitationEntity>) : List<UserInvitation> {
    val resultingList = arrayListOf<UserInvitation>()
    users.forEach { user ->
        resultingList.add(
            UserInvitation(
                user = user,
                invitationEntity = invitations.firstOrNull { invite -> invite.invitedEmail == user.email }
            )
        )
    }
    invitations.filter { invite -> resultingList.none { r -> r.invitationEntity?.id == invite.id } }.forEach {invitation ->
        resultingList.add(
            UserInvitation(
                invitationEntity = invitation,
                user = users.firstOrNull { user -> user.email == invitation.invitedEmail }
            )
        )
    }

    val filteredList = resultingList.filter { invitation ->
        (invitation.user?.email?.startsWith(searchQuery,ignoreCase = true) == true)
                || (invitation.invitationEntity?.invitedEmail?.startsWith(searchQuery, ignoreCase = true) == true)
    }

    return if(searchQuery.isNotBlank()) filteredList else resultingList
}



fun Activity.openAppSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package",packageName,null)
    ).also {
        startActivity(it)
    }
}


