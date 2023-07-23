package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.domain.use_cases.invitation.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllInvitationsByProjectId: GetAllInvitationsByProjectIdUseCase,
    private val upsertAllInvitationsUseCase: UpsertAllInvitationsUseCase,
    private val deleteAllInvitationsByProjectIdUseCase: DeleteAllInvitationsByProjectIdUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase
) : ViewModel() {


    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    fun getAllInvitationsByProjectId() : Flow<List<InvitationEntity>> = getAllInvitationsByProjectId(projectId)

}