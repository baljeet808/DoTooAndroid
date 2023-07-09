package com.baljeet.youdotoo.presentation.ui.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithEveryThing
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getProjectDoToosUseCase: GetProjectDoToosUseCase
) : ViewModel() {

    var projectState = mutableStateOf(ProjectWithEveryThing())
        private set

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    init {
        viewModelScope.launch {

            getProjectByIdAsFlowUseCase(projectId = projectId).collect { project ->
                projectState.value = projectState.value.copy(
                    project = project.toProject()
                )
                val ids = project.toProject().getUserIds()
                getUserProfiles(ids)
            }

            getProjectDoToosUseCase(projectId = projectId).collect { tasks ->
                projectState.value = projectState.value.copy(
                    doToos = tasks.map { it.toDoTooItem() }
                )
            }

        }
    }


    private fun getUserProfiles(ids : List<String>){
        val profiles = arrayListOf<User>()
        viewModelScope.launch {
            ids.forEach { userId ->
                getUserByIdUseCase(userId)?.let { user ->
                    profiles.add(user)
                }
            }
            if(projectState.value.profiles != profiles){
                projectState.value = projectState.value.copy(
                    profiles = profiles
                )
            }
        }
    }


    private fun isProjectIsSharedToUser(project: Project) =
        project.collaboratorIds.contains(SharedPref.userId) ||
    project.viewerIds.contains(SharedPref.userId)

    fun upsertDoToo(doTooItem: DoTooItem, project : Project){
        val newDoToo = doTooItem.copy()
        newDoToo.done = doTooItem.done.not()
        newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ").plus(if(newDoToo.done)"completed." else "not completed.")
        if(SharedPref.isUserAPro || isProjectIsSharedToUser(project)){
            projectsReference
                .document(projectId)
                .collection("todos")
                .document(newDoToo.id)
                .set(newDoToo)

        }
        viewModelScope.launch {
            upsertDoToosUseCase(listOf(newDoToo),projectId)
        }
    }
}