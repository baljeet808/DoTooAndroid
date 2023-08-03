package com.baljeet.youdotoo

import androidx.compose.runtime.mutableStateListOf
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
class MainViewModel @Inject constructor(
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

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeLast()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (isGranted.not()) {
            visiblePermissionDialogQueue.add(0, permission)
        }
    }


}