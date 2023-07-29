package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.use_cases.invitation.DeleteAllInvitationsByProjectIdUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.DeleteInvitationUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.GetAllInvitationsByProjectIdUseCase
import com.baljeet.youdotoo.domain.use_cases.invitation.UpsertAllInvitationsUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllInvitationsByProjectId: GetAllInvitationsByProjectIdUseCase,
    private val upsertAllInvitationsUseCase: UpsertAllInvitationsUseCase,
    private val deleteAllInvitationsByProjectIdUseCase: DeleteAllInvitationsByProjectIdUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {


    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    fun getAllInvitationsByProjectId() : Flow<List<InvitationEntity>> = getAllInvitationsByProjectId(projectId)

    fun getAllUsers(): Flow<List<UserEntity>> = getUsersUseCase()

}