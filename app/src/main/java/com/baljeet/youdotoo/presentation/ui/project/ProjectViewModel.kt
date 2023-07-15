package com.baljeet.youdotoo.presentation.ui.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersByIdsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val deleteDoToosUseCase: DeleteDoTooUseCase,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase,
    private val getUsersByIdsUseCase: GetUsersByIdsUseCase,
    private val getProjectDoToosUseCase: GetProjectDoToosUseCase
) : ViewModel() {


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])


    fun getProjectById() : Flow<ProjectEntity> = getProjectByIdAsFlowUseCase(projectId)

    fun getProjectTasks() : Flow<List<DoTooItemEntity>> = getProjectDoToosUseCase(projectId)

    fun getUserProfiles(ids : List<String>) : Flow<List<UserEntity>> = getUsersByIdsUseCase(ids)


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

        }else{
            viewModelScope.launch {
                upsertDoToosUseCase(listOf(newDoToo),projectId)
            }
        }
    }


    fun deleteTask(task : DoTooItem){
        if(SharedPref.isUserAPro){
            projectsReference
                .document(projectId)
                .collection("todos")
                .document(task.id)
                .delete()
        }else{
            viewModelScope.launch {
                deleteDoToosUseCase(task, projectId = projectId)
            }
        }
    }
}