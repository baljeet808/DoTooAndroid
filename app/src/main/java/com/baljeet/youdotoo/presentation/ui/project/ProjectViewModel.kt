package com.baljeet.youdotoo.presentation.ui.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.DeleteProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersByIdsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val deleteDoToosUseCase: DeleteDoTooUseCase,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase,
    private val getUsersByIdsUseCase: GetUsersByIdsUseCase,
    private val getProjectDoToosUseCase: GetProjectDoToosUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase
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
        if(SharedPref.isUserAPro || isProjectIsSharedToUser(project)){
            projectsReference
                .document(projectId)
                .collection("todos")
                .document(doTooItem.id)
                .set(doTooItem)

        }else{
            CoroutineScope(Dispatchers.IO).launch {
                upsertDoToosUseCase(listOf(doTooItem),projectId)
            }
        }
        upsertProject(project)
    }

    fun upsertProject(project : Project){
        val projectCopy = project.copy()

        projectCopy.updatedAt = getSampleDateInLong()
        if(SharedPref.isUserAPro || isProjectIsSharedToUser(project)){
            projectsReference
                .document(projectId)
                .set(projectCopy)
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                upsertProjectUseCase(listOf(projectCopy))
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
        }
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoToosUseCase(task, projectId = projectId)
        }
    }

    fun deleteProject(project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            deleteProjectUseCase(project = project)
        }
        if(SharedPref.isUserAPro){
            projectsReference
                .document(projectId)
                .delete()
        }

    }
}