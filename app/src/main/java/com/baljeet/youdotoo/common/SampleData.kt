package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.util.UUID
import kotlin.random.Random


fun getSampleProfile(): User {
    return User(
        id = "NuZXwLl3a8O3mXRcXFsJzHQgB172",
        name = "Baljeet Singh",
        email = "baljeet88sandhu@gmail.com",
        joined = getSampleDateInLong(),
        avatarUrl = getRandomAvatar()
    )
}

fun getRandomAvatar(): String {
    val randomInt = Random.nextInt(from = 0, 6)
    val avatars = arrayListOf<String>(
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/20.png?alt=media&token=fa1489d4-8951-4ef6-8f96-862938aedb62",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/1.png?alt=media&token=b15d14e9-722d-410d-b9b4-23682b5773f3",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/2.png?alt=media&token=68e95bc9-9553-4027-90d9-af688b9fd0f4",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/16.png?alt=media&token=160bd284-4b6a-488c-b66b-b421ebde9c21",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/4.png?alt=media&token=3c06a69a-7f00-4238-b6cf-3296f3532576",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/6.png?alt=media&token=3fefb778-d6a4-4c05-9d0b-ed8dde6091e5",
        "https://firebasestorage.googleapis.com/v0/b/youdotoo-81372.appspot.com/o/9.png?alt=media&token=dd24b271-e068-43a6-b0f4-c3142873575c"
    )
    return avatars[randomInt]
}


fun getRandomId(): String {
    return UUID.randomUUID().toString()
}

fun getSampleDotooItem(): TaskEntity {
    return TaskEntity(
        id = getRandomId(),
        title = "Wash the dishes Please",
        description = "To see the preview comment code related to viewModel",
        dueDate = 90L,
        createDate = 789L,
        done = Random.nextInt(from = 0, 1) == 0,
        priority = "High",
        updatedBy = "Baljeet create this doToo item.",
        projectId = ""
    )
}

fun getSampleTaskWithProject() : TaskWithProject {
    return TaskWithProject(
        task = getSampleDotooItem(),
        projectEntity = getSampleProject().toProjectEntity()
    )
}

fun getSampleProject(): Project {
    return Project(
        id = getRandomId(),
        name = "Daily long long chores",
        description = "This project is about the irritating stuff which always gets forgotten.",
        ownerId = "",
        collaboratorIds = getSampleIds(),
        viewerIds = getSampleIds(),
        update = "",
        color = getRandomColor(),
        updatedAt = getSampleDateInLong()
    )
}

fun getSampleProjectWithTasks(): ProjectWithDoToos {
    return ProjectWithDoToos(
        project = getSampleProject().toProjectEntity(),
        tasks = listOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem(),
        )
    )
}

fun getRandomColor(): Long {
    val randomInt = Random.nextInt(from = 0, 9)
    return EnumProjectColors.values()[randomInt].longValue
}


fun getRandomColorEnum(): EnumProjectColors {
    val randomInt = Random.nextInt(from = 0, 9)
    return EnumProjectColors.values()[randomInt]
}

fun getSampleMessage(): MessageEntity {
    return MessageEntity(
        id = getRandomId(),
        message = "Hey there, This is a new message about your doToo. What do you think.",
        senderId = "NuZXwLl3a8O3mXRcXFsJzHQgB172",
        createdAt = getSampleDateInLong(),
        isUpdate = false,
        attachmentUrl = getRandomAvatar(),
        attachmentName = null,
        interactions = getSampleInteractions().joinToString(" | "),
        projectId = getRandomId()
    )
}

fun getSampleDateInLong() = LocalDateTime.now().toKotlinLocalDateTime()
    .toInstant(TimeZone.currentSystemDefault()).epochSeconds

fun getSampleInteractions(): ArrayList<String> {
    return arrayListOf(
        "iz8dz6PufNPGbw9DzWUiZyoTHn62,feeling_good",
        "NuZXwLl3a8O3mXRcXFsJzHQgB172,wow",
        "NuZXwLl3a8O3mXRcXFsJzHQgB172,sad_face"
    )
}

fun getSampleIds(): List<String> {
    return listOf(
        getRandomId(),
        getRandomId()
    )
}

fun getSampleInvitation(): InvitationEntity{
    return InvitationEntity(
        id = getRandomId(),
        inviteeId = getRandomId(),
        projectId = getRandomId(),
        status = InvitationPending,
        accessType = Random.nextInt(from = 0, 1),
        invitedEmail = getSampleProfile().email,
        inviteeName = "Baljeet singh",
        projectName = "App error list.",
        projectDetail = "List of all app errors which need some fixing.",
        projectColor = getRandomColor()
    )
}

fun getSampleMessageNotification() : NotificationEntity{
    return NotificationEntity(
        id = getRandomId(),
        title = "Message from Baljeet Singh",
        contentText = "Hi there guys, hope you are seeing notifications all well. Please write more suggestion for this task in chat here.",
        invitationId = null,
        projectId = "",
        taskId = "",
        messageId = "",
        createdAt = getSampleDateInLong(),
        projectColor = getRandomColor(),
        isNew = true,
        notificationType = EnumNotificationType.NewMessage
    )
}
fun getSampleInvitationNotification() : NotificationEntity{
    return NotificationEntity(
        id = getRandomId(),
        title = "Project Invitation",
        contentText = "Karandeep kaur has invited you to a project. Tap to see more details about project",
        invitationId = "",
        projectId = "",
        taskId = null,
        messageId = null,
        createdAt = getSampleDateInLong(),
        projectColor = getRandomColor(),
        isNew = true,
        notificationType = EnumNotificationType.NewInvitation
    )
}


fun getSampleColorPalette(): ColorPaletteEntity{
    return ColorPaletteEntity(
        id = getRandomId(),
        paletteName = "Randomized",
        nightDark = getRandomColor(),
        nightLight = getRandomColor(),
        dayDark = getRandomColor(),
        dayLight = getRandomColor()
    )
}