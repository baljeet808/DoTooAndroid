package com.baljeet.youdotoo.presentation.ui.projectinvitation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.AccessTypeEditor
import com.baljeet.youdotoo.common.AccessTypeViewer
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationDeclined
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.invitation.GetInvitationByIdAsFlowUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectInvitationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getInvitationByIdAsFlowUseCase: GetInvitationByIdAsFlowUseCase
) : ViewModel() {

    private val onlineDB = FirebaseFirestore.getInstance()

    private val invitationId: String = checkNotNull(savedStateHandle["invitationId"])

    fun getInvitationByIdAsFlow() = getInvitationByIdAsFlowUseCase(invitationId)

    private val invitationReference =
        onlineDB.collection("invitations")

    fun acceptInvitation(invitation: InvitationEntity) {
        val updatedInvitation = invitation.copy()
        updatedInvitation.status = InvitationAccepted
        invitationReference
            .document(invitation.id)
            .set(updatedInvitation)
            .addOnSuccessListener {
                addUserToProject(invitation.projectId, invitation.accessType)
            }
    }

    fun declineInvitation(invitation: InvitationEntity) {
        val updatedInvitation = invitation.copy(
            status = InvitationDeclined
        )
        invitationReference
            .document(invitation.id)
            .set(updatedInvitation)
    }

    private fun addUserToProject(projectId: String, accessType: Int) {

        val projectReference = onlineDB
            .collection("projects")


        projectReference.document(projectId).get().addOnSuccessListener { projectSnapshot ->
            val project = Project(
                id = projectSnapshot.getString("id") ?: "",
                name = projectSnapshot.getString("name") ?: "",
                description = projectSnapshot.getString("description") ?: "",
                ownerId = projectSnapshot.getString("ownerId") ?: "",
                viewerIds = (projectSnapshot.get("viewerIds") as List<String>),
                collaboratorIds = (projectSnapshot.get("collaboratorIds") as List<String>),
                update = projectSnapshot.getString("update") ?: "",
                color = projectSnapshot.getString("color") ?: getRandomColor(),
                updatedAt = projectSnapshot.getLong("updatedAt") ?: getSampleDateInLong()
            )

            var editorIDs = arrayListOf<String>()
            var viewerIDs = arrayListOf<String>()

            when (accessType) {
                AccessTypeViewer -> {
                    viewerIDs = project.viewerIds.toCollection(ArrayList())
                    viewerIDs.add(SharedPref.userId!!)
                }

                AccessTypeEditor -> {
                    editorIDs = project.collaboratorIds.toCollection(ArrayList())
                    editorIDs.add(SharedPref.userId!!)
                }
            }

            val updatedProject = project.copy(
                viewerIds = viewerIDs,
                collaboratorIds = editorIDs
            )

            projectReference.document(projectId).set(updatedProject)
        }
    }
}