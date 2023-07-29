package com.baljeet.youdotoo.presentation.ui.invitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.AccessTypeAdmin
import com.baljeet.youdotoo.common.AccessTypeEditor
import com.baljeet.youdotoo.common.AccessTypeViewer
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationPending
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.UserInvitation
import com.baljeet.youdotoo.domain.use_cases.invitation.DeleteInvitationUseCase
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
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val deleteInvitationUseCase: DeleteInvitationUseCase
) : ViewModel() {


    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    private var project : ProjectEntity? = null

    private var invitationsReference = FirebaseFirestore
        .getInstance()
        .collection("invitations")

    private var projectReference = FirebaseFirestore
        .getInstance()
        .collection("projects")
        .document(projectId)


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

    /**
     * If invitee already has declined the invitation previously then this
     * method will reincarnate that invitation
     * **/
    fun resendInvitation(invitation: InvitationEntity){

    }

    /**
     * If user already added to the project then this method will remove user from
     * the project and then notify user about this change and then delete the invitation completely.
     * after which that notification will live in user's local data
     * **/
    fun removeUserFromProject(userInvitation: UserInvitation){

    }

    /**
     * This method will update the access of user in project
     * and will notify user about it
     * **/
    fun updateUserAccessType(userInvitation: UserInvitation, accessType: Int){
        if(SharedPref.isUserAPro){
            val invitation = userInvitation.invitationEntity!!

            if(invitation.status == InvitationAccepted){

                project?.let { projectEntity ->
                    var editorIDs = arrayListOf<String>()
                    var viewerIDs = arrayListOf<String>()

                    when (accessType) {
                        AccessTypeViewer -> {
                            editorIDs = projectEntity.collaboratorIds.split(",").toCollection(ArrayList())
                            editorIDs.remove(userInvitation.user?.id ?: "")

                            viewerIDs = projectEntity.viewerIds.split(",").toCollection(ArrayList())
                            viewerIDs.add(userInvitation.user?.id ?: "")
                        }

                        AccessTypeEditor -> {
                            editorIDs = projectEntity.collaboratorIds.split(",").toCollection(ArrayList())
                            editorIDs.add(userInvitation.user?.id ?: "")

                            viewerIDs = projectEntity.viewerIds.split(",").toCollection(ArrayList())
                            viewerIDs.remove(userInvitation.user?.id ?: "")
                        }

                        AccessTypeAdmin -> {
                            editorIDs = projectEntity.collaboratorIds.split(",").toCollection(ArrayList())
                            editorIDs.remove(userInvitation.user?.id ?: "")

                            viewerIDs = projectEntity.viewerIds.split(",").toCollection(ArrayList())
                            viewerIDs.remove(userInvitation.user?.id ?: "")
                        }
                    }

                    val updatedProject = projectEntity.copy(
                        viewerIds = viewerIDs.joinToString(separator = ","),
                        collaboratorIds = editorIDs.joinToString(separator = ",")
                    )

                    if(accessType == AccessTypeAdmin){
                        updatedProject.ownerId = userInvitation.user?.id?:SharedPref.userId!!
                    }

                    projectReference.set(updatedProject)
                }
            }

            val updatedInvitation = invitation.copy(
                accessType = accessType
            )

            invitationsReference
                .document(updatedInvitation.id)
                .set(updatedInvitation)
        }
    }

    /**
     * Delete the given invitation and notify invited user about it
     * **/
    fun deleteInvitation(invitation : InvitationEntity){
        invitationsReference
            .document(invitation.id)
            .delete()
            .addOnSuccessListener {
                viewModelScope.launch {
                    deleteInvitationUseCase(invitation)
                }
            }
    }

}