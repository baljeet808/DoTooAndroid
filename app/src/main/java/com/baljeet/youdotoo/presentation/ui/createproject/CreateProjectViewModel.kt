package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val upsertProjectUseCase: UpsertProjectUseCase
) : ViewModel() {

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    fun createProject(projectName: String, description: String?, projectColor: Long) {
        val newProject = Project(
            id = UUID.randomUUID().toString(),
            ownerId = SharedPref.userId!!,
            name = projectName,
            description = description ?: "",
            collaboratorIds = listOf(),
            viewerIds = listOf(),
            update = "${SharedPref.userName} created new project named '${projectName}.'",
            color = projectColor,
            updatedAt = getSampleDateInLong()
        )
        createProject(newProject)
    }


    private fun createProject(project : Project){
        if(SharedPref.isUserAPro){
            createProjectOnServer(project)
        }else{
            createProjectLocally(project)
        }
    }

    private fun createProjectOnServer(project: Project) {
        projectsReference
            .document(project.id)
            .set(project)
    }

    private fun createProjectLocally(project: Project) {
        CoroutineScope(Dispatchers.IO).launch {
            upsertProjectUseCase(listOf(project))
        }
    }

}