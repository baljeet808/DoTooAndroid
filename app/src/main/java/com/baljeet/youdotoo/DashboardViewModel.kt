package com.baljeet.youdotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.EnumNotificationType
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationArchived
import com.baljeet.youdotoo.common.InvitationDeclined
import com.baljeet.youdotoo.common.InvitationPending
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.database_operations.DeleteAllTablesUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteTasksByProjectIdUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectTasksUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.DeleteInvitationUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.GetInvitationByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.UpsertAllInvitationsUseCase
import com.baljeet.youdotoo.domain.use_cases.messages.DeleteAllMessagesOfProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.messages.UpsertMessagesUseCase
import com.baljeet.youdotoo.domain.use_cases.notifications.UpsertNotificationsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.DeleteProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.users.UpsertUserUseCase
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
    private val upsertNotificationsUseCase: UpsertNotificationsUseCase,
    private val deleteAllTablesUseCase: DeleteAllTablesUseCase,
    private val upsertMessagesUseCase: UpsertMessagesUseCase,
    private val deleteAllMessagesOfProjectUseCase: DeleteAllMessagesOfProjectUseCase,
    private val getProjectTasksUseCase: GetProjectTasksUseCase,
    private val deleteDoTooUseCase: DeleteDoTooUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
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
                val allOnlineProjects = arrayListOf<Project>()
                for (project in snapshot) {
                    val onlineProject = Project(
                        id = project.getString("id") ?: "",
                        name = project.getString("name") ?: "",
                        description = project.getString("description") ?: "",
                        ownerId = project.getString("ownerId") ?: "",
                        viewerIds = (project.get("viewerIds") as List<String>),
                        collaboratorIds = (project.get("collaboratorIds") as List<String>),
                        update = project.getString("update") ?: "",
                        color = project.getString("color") ?: "Indigo",
                        updatedAt = project.getLong("updatedAt") ?: getSampleDateInLong()
                    )
                    //Saving it to list
                    allOnlineProjects.add(onlineProject)
                    // save all online projects to local db
                    CoroutineScope(Dispatchers.IO).launch {
                        upsertProjectUseCase.invoke(arrayListOf(onlineProject.toProjectEntity()))
                    }

                    projectsReference
                        .document(onlineProject.id)
                        .collection("todos")
                        .addSnapshotListener { sShot, e ->
                            if (sShot != null && e == null) {
                                val tasks = ArrayList<TaskEntity>()
                                for (task in sShot) {
                                    tasks.add(
                                        TaskEntity(
                                            id = task.getString("id") ?: "",
                                            title = task.getString("title") ?: "",
                                            description = task.getString("description") ?: "",
                                            createDate = task.getLong("createDate") ?: 0L,
                                            dueDate = task.getLong("dueDate") ?: 0L,
                                            priority = task.getString("priority") ?: "High",
                                            updatedBy = task.getString("updatedBy") ?: "",
                                            done = task.getBoolean("done") ?: false,
                                            projectId = task.getString("projectId")?:""
                                        )
                                    )
                                }
                                /**
                                 * Code to delete tasks locally which have been deleted online
                                 * **/
                                CoroutineScope(Dispatchers.IO).launch {
                                    val allLocalTasksOfThisProject = getProjectTasksUseCase(onlineProject.id)
                                    upsertDoToosUseCase(
                                        tasks = tasks
                                    )
                                    if(allLocalTasksOfThisProject.size != tasks.size){
                                        allLocalTasksOfThisProject.filter { localTask -> tasks.none { onlineTask -> onlineTask.id == localTask.id } }.forEach { wildTask ->
                                            deleteDoTooUseCase(wildTask)
                                        }
                                    }
                                }
                            }
                            if (e != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteTasksByProjectId(onlineProject.id)
                                }
                            }
                        }


                    projectsReference
                        .document(onlineProject.id)
                        .collection("messages")
                        .addSnapshotListener{ snapShot , e ->
                            if (snapShot != null && e == null) {
                                val messages = ArrayList<MessageEntity>()
                                for (message in snapShot) {
                                    messages.add(
                                        MessageEntity(
                                            id = message.getString("id") ?: "",
                                            senderId = message.getString("senderId")?: "",
                                            message = message.getString("message")?: "",
                                            createdAt = message.getLong("createdAt") ?: getSampleDateInLong(),
                                            isUpdate = message.getBoolean("isUpdate")?: false,
                                            attachmentUrl = message.getString("attachmentUrl"),
                                            attachmentName = message.getString("attachmentName"),
                                            interactions = message.getString("interactions")?:"",
                                            projectId = message.getString("projectId")?: ""
                                        )
                                    )
                                }
                                CoroutineScope(Dispatchers.IO).launch {
                                    upsertMessagesUseCase(messages)
                                }
                            }
                            if (e != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteAllMessagesOfProjectUseCase(onlineProject.id)
                                }
                            }
                        }


                    getUserProfilesAndUpdateDatabase(onlineProject)
                }

                /**
                 * Code to delete projects locally which have been deleted online
                 * **/
                CoroutineScope(Dispatchers.IO).launch {
                    val allLocalProject = getProjectsUseCase()
                    if(allOnlineProjects.size < allLocalProject.filter { getRole(it.toProject()) != EnumRoles.Admin }.size){
                        allLocalProject.filter { getRole(it.toProject()) != EnumRoles.Admin }.filter { localProject -> allOnlineProjects.none { onlineProject -> onlineProject.id == localProject.id } }.forEach { wildProject ->
                            deleteProjectUseCase(wildProject.toProject())
                        }
                    }
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
                            projectColor = invitation.getString("projectColor")
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
                                                        notificationType = EnumNotificationType.NewInvitation
                                                    )
                                                )
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
                                                        notificationType = EnumNotificationType.InvitationUpdate
                                                    )
                                                )
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
                                                        notificationType = EnumNotificationType.InvitationUpdate
                                                    )
                                                )
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
                                            notificationType = EnumNotificationType.NewInvitation
                                        )
                                    )
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
                                    joined = data.getLong("joined") ?: 0L,
                                    firebaseToken = data.getString("firebaseToken")?:""
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

    fun clearEverything(){
        CoroutineScope(Dispatchers.IO).launch {
            deleteAllTablesUseCase()
        }
    }

}