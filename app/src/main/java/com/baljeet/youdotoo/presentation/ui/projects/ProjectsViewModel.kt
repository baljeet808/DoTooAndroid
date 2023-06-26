package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import com.baljeet.youdotoo.domain.models.User
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel @Inject constructor() : ViewModel() {

    var projectState = mutableStateOf<List<ProjectWithProfiles>>(listOf())
        private set



    var db = Firebase.firestore

    var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")
        .where(
            Filter.or(
                Filter.equalTo("ownerId", SharedPref.userId),
                Filter.arrayContains("collaboratorIds", SharedPref.userId),
                Filter.arrayContains("viewerIds", SharedPref.userId)
            )
        )

    init {
        projectsReference.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {
                val projects = ArrayList<Project>()
                for (project in snapshot) {
                    projects.add(
                        Project(
                            id = project.getString("id") ?: "",
                            name = project.getString("name") ?: "",
                            description = project.getString("description") ?: "",
                            ownerId = project.getString("ownerId") ?: "",
                            viewerIds = (project.get("viewerIds") as List<String>),
                            collaboratorIds = (project.get("collaboratorIds") as List<String>),
                        )
                    )
                }
                fetchProfilesForProjects(projects)
            }
        }
    }

    private fun fetchProfilesForProjects(projects: List<Project>) {
        val projectsWithProfiles = arrayListOf<ProjectWithProfiles>()
        viewModelScope.launch {
            for (project in projects) {
                val profiles = getUserProfiles(project)
                projectsWithProfiles.add(
                    ProjectWithProfiles(
                        profiles = if (profiles.isNotEmpty()) profiles else null,
                        project = project
                    )
                )
            }
            projectState.value = projectsWithProfiles
        }
    }

    suspend fun getUserProfiles(project: Project): ArrayList<User> {

        val ids = if (project.collaboratorIds.isNotEmpty()) {
            project.collaboratorIds.toCollection(ArrayList())
        } else {
            arrayListOf()
        }
        if (project.viewerIds.isNotEmpty()) {
            ids.addAll(project.viewerIds)
        }
        val users = ArrayList<User>()
        for (userId in ids) {
            db.collection("users").whereEqualTo("id", userId).get()
                .addOnSuccessListener { documents ->
                    documents.first()?.let { data ->
                        users.add(
                            User(
                                id = data.getString("id") ?: "",
                                name = data.getString("name") ?: "",
                                email = data.getString("description") ?: "",
                                avatarUrl = data.getString("avatarUrl") ?: "",
                                joined = data.getLong("joined") ?: 0L
                            )
                        )
                    }
                }.await()
        }
        return users
    }
}