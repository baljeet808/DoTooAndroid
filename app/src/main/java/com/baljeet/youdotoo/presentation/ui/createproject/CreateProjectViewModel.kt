package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateProjectViewModel @Inject constructor(
    private val upsertProjectUseCase: UpsertProjectUseCase
) : ViewModel() {


    var createState = mutableStateOf(false)
        private set

    var db = Firebase.firestore

    fun createProject(projectName: String, description: String?, projectColor: Long) {
        val newProjectId = UUID.randomUUID().toString()
        val projectRef = db.collection("projects")

        val newProject = Project(
            id = newProjectId,
            ownerId = SharedPref.userId!!,
            name = projectName,
            description = description ?: "",
            collaboratorIds = listOf(),
            viewerIds = listOf(),
            update = "${SharedPref.userName} created new project named '${projectName}.'",
            color = projectColor
        )

        if(SharedPref.isUserAPro) {
            projectRef
                .document(newProjectId)
                .set(newProject)
        }else{
            viewModelScope.launch {
                upsertProjectUseCase(listOf(newProject))
            }
        }

    }

    fun resetState(){
        createState.value = false
    }

}