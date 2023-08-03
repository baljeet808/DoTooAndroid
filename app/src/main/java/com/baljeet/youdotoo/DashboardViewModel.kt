package com.baljeet.youdotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.AccessTypeAdmin
import com.baljeet.youdotoo.common.AccessTypeEditor
import com.baljeet.youdotoo.common.AccessTypeViewer
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationArchived
import com.baljeet.youdotoo.common.InvitationDeclined
import com.baljeet.youdotoo.common.InvitationPending
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteTasksByProjectIdUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.DeleteInvitationUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.GetInvitationByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.UpsertAllInvitationsUseCase
import com.baljeet.youdotoo.domain.use_cases.notifications.UpsertNotificationsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.users.UpsertUserUseCase
import com.baljeet.youdotoo.services.InvitationNotificationService
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deleteTasksByProjectId: DeleteTasksByProjectIdUseCase,
    private val upsertAllInvitationsUseCase: UpsertAllInvitationsUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val upsertUserUseCase: UpsertUserUseCase,
    private val getInvitationByIdUseCase: GetInvitationByIdUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase,
    private val invitationsNotificationService: InvitationNotificationService,
    private val upsertNotificationsUseCase: UpsertNotificationsUseCase
) : ViewModel() {


    private var db = Firebase.firestore


    private var invitationsReference = FirebaseFirestore
        .getInstance()
        .collection("invitations")

    private var invitationsQuery: Query = invitationsReference.where(
        Filter.or(
            Filter.equalTo("inviteeId", SharedPref.userId),
            Filter.equalTo("invitedEmail", SharedPref.userEmail)
        )
    )


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private var projectsQuery: Query = projectsReference


    init {

        /**
         * if user is pro change query to fetch all projects from firestore
         * else only fetch the projects which are 'shared to' user from other users
         * **/
        if (SharedPref.isUserAPro) {
            projectsQuery = projectsReference.where(
                Filter.or(
                    Filter.equalTo("ownerId", SharedPref.userId),
                    Filter.arrayContains("collaboratorIds", SharedPref.userId),
                    Filter.arrayContains("viewerIds", SharedPref.userId)
                )
            )
        } else {
            projectsQuery = projectsReference.where(
                Filter.or(
                    Filter.arrayContains("collaboratorIds", SharedPref.userId),
                    Filter.arrayContains("viewerIds", SharedPref.userId)
                )
            )
        }


        /**
         * Connect user to firestore online projects
         * and their tasks. and save/update everything to local db
         * **/
        projectsQuery.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {
                for (project in snapshot) {
                    val onlineProject = Project(
                        id = project.getString("id") ?: "",
                        name = project.getString("name") ?: "",
                        description = project.getString("description") ?: "",
                        ownerId = project.getString("ownerId") ?: "",
                        viewerIds = (project.get("viewerIds") as List<String>),
                        collaboratorIds = (project.get("collaboratorIds") as List<String>),
                        update = project.getString("update") ?: "",
                        color = project.getLong("color") ?: 4278215265,
                        updatedAt = project.getLong("updatedAt") ?: getSampleDateInLong()
                    )

                    // save all online projects to local db
                    CoroutineScope(Dispatchers.IO).launch {
                        upsertProjectUseCase.invoke(arrayListOf(onlineProject))
                    }

                    projectsReference
                        .document(onlineProject.id)
                        .collection("todos")
                        .addSnapshotListener { sShot, e ->
                            if (sShot != null && e == null) {
                                val doToos = ArrayList<DoTooItem>()
                                for (doToo in sShot) {
                                    doToos.add(
                                        DoTooItem(
                                            id = doToo.getString("id") ?: "",
                                            title = doToo.getString("title") ?: "",
                                            description = doToo.getString("description") ?: "",
                                            createDate = doToo.getLong("createDate") ?: 0L,
                                            dueDate = doToo.getLong("dueDate") ?: 0L,
                                            priority = doToo.getString("priority") ?: "High",
                                            updatedBy = doToo.getString("updatedBy") ?: "",
                                            done = doToo.getBoolean("done") ?: false,
                                            projectColor = doToo.getLong("projectColor")
                                                ?: getRandomColor()
                                        )
                                    )
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    upsertDoToosUseCase(
                                        dotoos = doToos,
                                        projectId = onlineProject.id
                                    )
                                }
                            }
                            if (e != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteTasksByProjectId(onlineProject.id)
                                }
                            }
                        }
                    getUserProfilesAndUpdateDatabase(onlineProject)
                }
            }
        }


        invitationsQuery.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {

                val invitations = arrayListOf<InvitationEntity>()
                for (invitation in snapshot) {
                    invitations.add(
                        InvitationEntity(
                            id = invitation.getString("id") ?: "",
                            inviteeId = invitation.getString("inviteeId") ?: "",
                            inviteeName = invitation.getString("inviteeName") ?: "",
                            invitedEmail = invitation.getString("invitedEmail") ?: "",
                            projectId = invitation.getString("projectId") ?: "",
                            projectName = invitation.getString("projectName") ?: "",
                            accessType = (invitation.getLong("accessType") ?: 0).toInt(),
                            status = (invitation.getLong("status") ?: 0).toInt(),
                            projectDetail = invitation.getString("projectDetail") ?: "",
                            projectColor = invitation.getLong("projectColor")
                                ?: getRandomColor()
                        )
                    )
                }

                invitations.forEach { invite ->
                    CoroutineScope(Dispatchers.IO).launch {
                        getInvitationByIdUseCase(invite.id)?.let { oldInvite ->
                            if (invite.status != oldInvite.status) {
                                when (invite.status) {
                                    InvitationPending -> {
                                        if (invite.invitedEmail == SharedPref.userEmail) {
                                            val title = "Project Invitation."
                                            val contextText =
                                                "${invite.inviteeName} has invited you to a project." +
                                                        "Tap to ee more details about project."

                                            upsertNotificationsUseCase(
                                                listOf(
                                                    NotificationEntity(
                                                        id = getRandomId(),
                                                        title = title,
                                                        contentText = contextText,
                                                        invitationId = invite.id,
                                                        projectId = invite.projectId,
                                                        taskId = null,
                                                        messageId = null,
                                                        createdAt = getSampleDateInLong(),
                                                        projectColor = invite.projectColor,
                                                        isNew = true,
                                                        notificationType = EnumNotificationType.INVITATION
                                                    )
                                                )
                                            )

                                            invitationsNotificationService.showInviteNotification(
                                                invite,
                                                title = title,
                                                contentText = contextText
                                            )
                                        }
                                    }

                                    InvitationAccepted -> {
                                        if (invite.inviteeId == SharedPref.userId) {
                                            val status = "accepted"
                                            val title = "Invitation $status."
                                            val contextText =
                                                "${invite.invitedEmail} has $status your invitation."

                                            upsertNotificationsUseCase(
                                                listOf(
                                                    NotificationEntity(
                                                        id = getRandomId(),
                                                        title = title,
                                                        contentText = contextText,
                                                        invitationId = invite.id,
                                                        projectId = invite.projectId,
                                                        taskId = null,
                                                        messageId = null,
                                                        createdAt = getSampleDateInLong(),
                                                        projectColor = invite.projectColor,
                                                        isNew = true,
                                                        notificationType = EnumNotificationType.INVITATION
                                                    )
                                                )
                                            )

                                            invitationsNotificationService.showInvitationResponseNotification(
                                                invite,
                                                title = title,
                                                contentText = contextText
                                            )
                                        }
                                    }

                                    InvitationDeclined -> {
                                        if (invite.inviteeId == SharedPref.userId) {
                                            val status = "declined"
                                            val title = "Invitation $status."
                                            val contextText =
                                                "${invite.invitedEmail} has $status your invitation."

                                            upsertNotificationsUseCase(
                                                listOf(
                                                    NotificationEntity(
                                                        id = getRandomId(),
                                                        title = title,
                                                        contentText = contextText,
                                                        invitationId = invite.id,
                                                        projectId = invite.projectId,
                                                        taskId = null,
                                                        messageId = null,
                                                        createdAt = getSampleDateInLong(),
                                                        projectColor = invite.projectColor,
                                                        isNew = true,
                                                        notificationType = EnumNotificationType.INVITATION
                                                    )
                                                )
                                            )

                                            invitationsNotificationService.showInvitationResponseNotification(
                                                invite,
                                                title = title,
                                                contentText = contextText
                                            )
                                        }
                                    }

                                    InvitationArchived -> {
                                        if (invite.invitedEmail == SharedPref.userEmail) {
                                            deleteInvitationUseCase(invite)
                                        }
                                    }
                                }
                            }

                            if (invite.accessType != oldInvite.accessType) {
                                if (invite.invitedEmail == SharedPref.userEmail) {
                                    val accessName = when (invite.accessType) {
                                        AccessTypeAdmin -> {
                                            "Admin"
                                        }

                                        AccessTypeEditor -> {
                                            "Editor"
                                        }

                                        AccessTypeViewer -> {
                                            "Viewer"
                                        }

                                        else -> return@launch
                                    }
                                    invitationsNotificationService.showAccessUpdateNotification(
                                        invite,
                                        accessName
                                    )
                                }
                            }

                        } ?: kotlin.run{
                            if (invite.invitedEmail == SharedPref.userEmail) {
                                val title = "Project Invitation."
                                val contextText =
                                    "${invite.inviteeName} has invited you to a project." +
                                            "Tap to ee more details about project."

                                upsertNotificationsUseCase(
                                    listOf(
                                        NotificationEntity(
                                            id = getRandomId(),
                                            title = title,
                                            contentText = contextText,
                                            invitationId = invite.id,
                                            projectId = invite.projectId,
                                            taskId = null,
                                            messageId = null,
                                            createdAt = getSampleDateInLong(),
                                            projectColor = invite.projectColor,
                                            isNew = true,
                                            notificationType = EnumNotificationType.INVITATION
                                        )
                                    )
                                )

                                invitationsNotificationService.showInviteNotification(
                                    invite,
                                    title = title,
                                    contentText = contextText
                                )
                            }
                        }
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    upsertAllInvitationsUseCase(invitations)
                }
            }
        }

    }


    private fun getUserProfilesAndUpdateDatabase(project: Project) {

        val ids = project.getUserIds()

        CoroutineScope(Dispatchers.IO).launch {
            ids.forEach { userId ->
                //only fetch from firestore if user is not in the local DB
                getUserByIdUseCase(userId) ?: kotlin.run {
                    db.collection("users").whereEqualTo("id", userId).get()
                        .addOnSuccessListener { documents ->
                            documents.first()?.let { data ->
                                val user = User(
                                    id = data.getString("id") ?: "",
                                    name = data.getString("name") ?: "",
                                    email = data.getString("email") ?: "",
                                    avatarUrl = data.getString("avatarUrl") ?: "",
                                    joined = data.getLong("joined") ?: 0L
                                )
                                //save user in local db
                                CoroutineScope(Dispatchers.IO).launch {
                                    upsertUserUseCase(listOf(user))
                                }
                            }
                        }.await()
                }
            }
        }

    }

    fun engageFun(){

    }
}