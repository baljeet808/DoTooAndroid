package com.baljeet.youdotoo.ui.createproject

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.models.Project
import com.baljeet.youdotoo.util.SharedPref
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.*

class CreateProjectViewModel: ViewModel() {

    private val _projectState = MutableStateFlow(ProjectViewStates())
    val projectState  = _projectState.asStateFlow()

    data class ProjectViewStates(
        var success : Boolean = false,
        var error : String? = null
    )


    var db = Firebase.firestore


    fun createProject(projectName : String, description : String?){
        if(canSave(projectName)){
            val newProjectId = UUID.randomUUID().toString()
            val projectRef = db.collection("projects")


            val newProject = Project(
                id = newProjectId,
                ownerId = SharedPref.userId!!,
                name = projectName,
                description = description?:"",
                collaboratorIds = listOf(),
                viewerIds = listOf()
            )

            projectRef
                .document(newProjectId)
                .set(newProject).addOnCompleteListener { Task ->
                    if(Task.isSuccessful){
                        _projectState.update {
                            it.copy(
                                success = true,
                                error = null
                            )
                        }
                    }else{
                        _projectState.update {
                            it.copy(
                                success = false,
                                error = "Something went wrong. Try again!"
                            )
                        }
                    }
                }
        }else{
            _projectState.update {
                it.copy(
                    success = false,
                    error = "Please fill required fields."
                )
            }
        }
    }

    private fun canSave(projectName: String): Boolean{
        return projectName.isNotBlank() && SharedPref.userId != null
    }
}