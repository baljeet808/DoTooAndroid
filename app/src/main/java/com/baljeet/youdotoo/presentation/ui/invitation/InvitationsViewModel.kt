package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.InvitationPending
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.use_cases.invitation.GetAllInvitationsByProjectIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAllInvitationsByProjectId: GetAllInvitationsByProjectIdUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase
) : ViewModel() {


    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    private var project : ProjectEntity? = null

    private var invitationsReference = FirebaseFirestore
        .getInstance()
        .collection("invitations")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            project = getProjectByIdUseCase(projectId)
        }
    }


    fun getAllInvitationsByProjectId() : Flow<List<InvitationEntity>> = getAllInvitationsByProjectId(projectId)

    fun getAllUsers(): Flow<List<UserEntity>> = getUsersUseCase()

    fun sendInvite(email : String, accessType : Int){
        if(SharedPref.isUserAPro && project != null){
            val newId = getRandomId()

            val newInvite = InvitationEntity(
                id = newId,
                projectName = project?.name?:"",
                projectId = projectId,
                inviteeName = SharedPref.userName,
                invitedEmail = email,
                accessType = accessType,
                status = InvitationPending,
                inviteeId = SharedPref.userId!!
            )

            invitationsReference
                .document(newId)
                .set(newInvite)

        }
    }

}