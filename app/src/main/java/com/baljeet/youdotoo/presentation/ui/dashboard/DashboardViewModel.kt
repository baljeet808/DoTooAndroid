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
class DashboardViewModel @Inject constructor (
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val upsertAllInvitationsUseCase: UpsertAllInvitationsUseCase
    ): ViewModel() {

    /**
     * fetch the all tasks from local database
     * **/
    fun allTasks(): Flow<List<DoTooItemEntity>> = getAllTasksUseCase()

    private var invitationsReference = FirebaseFirestore
        .getInstance()
        .collection("invitations")

    private var invitationsQuery: Query = invitationsReference.where(
        Filter.or(
            Filter.arrayContains("inviteeId", SharedPref.userId),
            Filter.arrayContains("invitedId", SharedPref.userId)
        )
    )

    init {
        invitationsQuery.addSnapshotListener { snapshot, error ->
            if(snapshot!= null && error == null){
                CoroutineScope(Dispatchers.IO).launch {
                    upsertAllInvitationsUseCase(snapshot.toObjects(InvitationEntity::class.java))
                }
            }
        }
    }
}