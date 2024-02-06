package com.baljeet.youdotoo.presentation.ui.accounts

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdAsFlowUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserByIdAsFlowUseCase: GetUserByIdAsFlowUseCase
) : ViewModel() {

    val firebaseAuth = FirebaseAuth.getInstance()

    fun userAsFlow() = getUserByIdAsFlowUseCase(SharedPref.userId!!)

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private var projectsQuery: Query = projectsReference

    init {
        projectsQuery = projectsReference.where(
            Filter.or(
                Filter.equalTo("ownerId", SharedPref.userId),
                Filter.arrayContains("collaboratorIds", SharedPref.userId),
                Filter.arrayContains("viewerIds", SharedPref.userId)
            )
        )
    }


    fun deleteEverything() {
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
                        color = project.getString("color") ?: getRandomColor() ,
                        updatedAt = project.getLong("updatedAt") ?: getSampleDateInLong()
                    )

                    if (onlineProject.ownerId == SharedPref.userId) {
                        projectsReference
                            .document(onlineProject.id)
                            .delete()
                    } else {
                        val editorIDs = onlineProject.collaboratorIds.toCollection(ArrayList())
                        editorIDs.remove(SharedPref.userId!!)

                        val viewerIDs = onlineProject.viewerIds.toCollection(ArrayList())
                        viewerIDs.remove(SharedPref.userId!!)

                        val updatedProject = onlineProject.copy(
                            viewerIds = viewerIDs,
                            collaboratorIds = editorIDs
                        )

                        projectsReference.document(updatedProject.id).set(updatedProject)
                    }
                }

                FirebaseFirestore
                    .getInstance()
                    .collection("users")
                    .document(SharedPref.userId!!)
                    .delete()

                firebaseAuth.signOut()
            }
        }
    }

}