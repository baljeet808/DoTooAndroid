package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.domain.models.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime


fun getSampleProjectWithEverything(): ProjectWithEveryThing{
    return ProjectWithEveryThing(
        project = getSampleProject() ,
        profiles = listOf(
           getSampleProfile(),
            getSampleProfile(),
            getSampleProfile()
        ),
        doToos = listOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        )
    )
}

fun getSampleDoTooWithProfiles(): DoTooWithProfiles{
    return  DoTooWithProfiles(
        project = getSampleProject(),
        doToo = getSampleDotooItem(),
        profiles = listOf(
            getSampleProfile(),
            getSampleProfile()
        )
    )
}


fun getSampleProfile(): User{
    return  User(
        id = "NuZXwLl3a8O3mXRcXFsJzHQgB172",
        name = "Baljeet Singh",
        email = "baljeet@gmail.com",
        joined = 82782L,
        avatarUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2Fdo2.png?alt=media&token=701d3864-68e3-445c-9c75-66bc06d44d09"
    )
}

fun getSampleDotooItem(): DoTooItem{
    return DoTooItem(
        id = "",
        title = "Wash the dishes Please",
        description = "To see the preview comment code related to viewModel",
        dueDate = 90L,
        createDate = 789L,
        done = true,
        priority = "High",
        updatedBy = "Baljeet create this doToo item."
    )
}

fun getSampleProject(): Project{
    return  Project(
        id = "",
        name = "Home Chores",
        description = "This project is about the irritating stuff which always gets forgotten.",
        ownerId = "",
        collaboratorIds = getSampleIds(),
        viewerIds = getSampleIds(),
        update = ""
    )
}

fun getSampleMessage(): Message{
    return Message(
        id = "",
        message = "Hey there, This is a new message about your doToo. What do you think.",
        senderId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
        createdAt = getSampleDateInLong(),
        isUpdate = false,
        attachmentUrl = null,
        interactions = getSampleInteractions()
    )
}

fun getSampleDateInLong() =  LocalDateTime.now().toKotlinLocalDateTime()
    .toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun getSampleInteractions(): ArrayList<String>{
    return arrayListOf(
        "iz8dz6PufNPGbw9DzWUiZyoTHn62,feeling_good",
        "NuZXwLl3a8O3mXRcXFsJzHQgB172,wow",
        "NuZXwLl3a8O3mXRcXFsJzHQgB172,sad_face"
    )
}

fun getSampleIds(): List<String>{
    return listOf(
            "iz8dz6PufNPGbw9DzWUiZyoTHn62",
    "NuZXwLl3a8O3mXRcXFsJzHQgB172"
    )
}