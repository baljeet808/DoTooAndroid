package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.UpsertAllInvitationsUseCase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val upsertAllInvitationsUseCase: UpsertAllInvitationsUseCase
) : ViewModel() {

    /**
     * fetch the all tasks from local database
     * **/
    fun allTasks(): Flow<List<DoTooItemEntity>> = getAllTasksUseCase()

    private var invitationsReference = FirebaseFirestore
        .getInstance()
        .collection("invitations")

    private var invitationsQuery: Query = invitationsReference.where(
        Filter.or(
            Filter.equalTo("inviteeId", SharedPref.userId),
            Filter.equalTo("invitedEmail", SharedPref.userEmail)
        )
    )

    init {
        invitationsReference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {
                CoroutineScope(Dispatchers.IO).launch {
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
                                status = (invitation.getLong("status") ?: 0).toInt()
                            )
                        )
                    }
                    upsertAllInvitationsUseCase(invitations)
                }
            }
        }
    }
}