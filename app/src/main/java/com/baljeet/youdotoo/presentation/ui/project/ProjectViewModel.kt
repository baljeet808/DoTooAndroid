package com.baljeet.youdotoo.presentation.ui.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.getRole
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



    fun upsertDoToo(task: DoTooItem, project : Project){
        when(getRole(project)){
            Roles.ProAdmin -> {
                updateTaskOnServerAndLocal(task, project)
            }
            Roles.Admin -> {
                updateTaskLocally(task, project)
            }
            Roles.Editor -> {
                updateTaskOnServerAndLocal(task, project)
            }
            Roles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            Roles.Blocked -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
        }
    }

    private fun updateTaskOnServerAndLocal(task : DoTooItem, project : Project){
        projectsReference
            .document(projectId)
            .collection("todos")
            .document(task.id)
            .set(task)
            .addOnCompleteListener {
                updateTaskLocally(task, project)
            }
    }

    private fun updateTaskLocally(task : DoTooItem, project : Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertDoToosUseCase(listOf(task),projectId)
            upsertProject(project)
        }
    }


    fun upsertProject(project : Project){
        val projectCopy = project.copy()
        projectCopy.updatedAt = getSampleDateInLong()

        when(getRole(project)){
            Roles.ProAdmin -> {
                updateProjectOnSeverAndLocally(project)
            }
            Roles.Admin -> {
                updateProjectLocally(project)
            }
            Roles.Editor -> {
                updateProjectOnSeverAndLocally(project)
            }
            Roles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            Roles.Blocked -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
        }
    }


    private fun updateProjectOnSeverAndLocally(project : Project){
        projectsReference
            .document(projectId)
            .set(project)
            .addOnCompleteListener {
                updateProjectLocally(project)
            }
    }

    private fun updateProjectLocally(project : Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertProjectUseCase(listOf(project))
        }
    }


    fun deleteProject(project: Project){

        when(getRole(project)){
            Roles.ProAdmin -> {
                deleteProjectOnServerAndLocally(project)
            }
            Roles.Admin -> {
                deleteProjectLocally(project)
            }
            Roles.Editor -> {
                deleteProjectOnServerAndLocally(project)
            }
            Roles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            Roles.Blocked -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
        }
    }

    private fun deleteProjectOnServerAndLocally(project: Project){
        projectsReference
            .document(projectId)
            .delete()
            .addOnCompleteListener {
                deleteProjectLocally(project)
            }
    }

    private fun deleteProjectLocally(project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            deleteProjectUseCase(project = project)
        }
    }

    fun deleteTask(task : DoTooItem, project: Project){
        when(getRole(project)){
            Roles.ProAdmin -> {
                deleteTaskOnServerAndLocally(task)
            }
            Roles.Admin -> {
                deleteTaskOnServerAndLocally(task)
            }
            Roles.Editor -> {
                deleteTaskOnServerAndLocally(task)
            }
            Roles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            Roles.Blocked -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
        }
    }
    private fun deleteTaskOnServerAndLocally(task: DoTooItem){
        projectsReference
            .document(projectId)
            .collection("todos")
            .document(task.id)
            .delete()
            .addOnCompleteListener {
                deleteTaskLocally(task)
            }
    }

    private fun deleteTaskLocally(task: DoTooItem){
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoToosUseCase(task, projectId = projectId)
        }
    }


}