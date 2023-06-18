package com.baljeet.youdotoo.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.models.ProjectWithProfiles
import com.baljeet.youdotoo.models.Project
import com.baljeet.youdotoo.models.User
import com.baljeet.youdotoo.util.SharedPref
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class ProjectsViewModel: ViewModel() {

    private val _projectState = MutableStateFlow(ProjectViewStates())
    val projectState  = _projectState.asStateFlow()

    data class ProjectViewStates(
        val projects : List<ProjectWithProfiles> = ArrayList()
    )


    var db = Firebase.firestore

    var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")
        .where(Filter.or(
            Filter.equalTo("ownerId",SharedPref.userId),
            Filter.arrayContains("collaboratorIds",SharedPref.userId),
            Filter.arrayContains("viewerIds",SharedPref.userId)
        ))

    init {
        projectsReference.addSnapshotListener { snapshot, error ->
            if(snapshot != null && error == null ){
                val projects = ArrayList<Project>()
                for (project in snapshot){
                    projects.add(Project(
                        id = project.getString("id")?:"",
                        name = project.getString("name")?:"",
                        description = project.getString("description")?:"",
                        ownerId = project.getString("ownerId")?:"",
                        viewerIds = (project.get("viewerIds") as List<String>),
                        collaboratorIds = (project.get("collaboratorIds") as List<String>),
                    )
                    )
                }
               fetchProfilesForProjects(projects)
            }
        }
    }




    private fun fetchProfilesForProjects(projects :List<Project>){
        val projectsWithProfiles = arrayListOf<ProjectWithProfiles>()
        viewModelScope.launch {
            for(project in projects) {
                val profiles = getUserProfiles(project)
                projectsWithProfiles.add(
                    ProjectWithProfiles(
                        profiles = if(profiles.isNotEmpty()) profiles else null,
                        project = project
                    )
                )
            }

            _projectState.update {
                it.copy(
                    projects = projectsWithProfiles
                )
            }
        }
    }

    suspend fun getUserProfiles(project : Project): ArrayList<User> {

        val ids = if(project.collaboratorIds.isNotEmpty()){
            project.collaboratorIds.toCollection(ArrayList())
        }else{
            arrayListOf()
        }
        if(project.viewerIds.isNotEmpty()){
            ids.addAll(project.viewerIds)
        }
        val users = ArrayList<User>()
        for (userId in ids) {
            db.collection("users").whereEqualTo("id",userId).get().addOnSuccessListener { documents ->
                documents.first()?.let { data->
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