package com.baljeet.youdotoo.presentation.ui.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectTasksAsFlowUseCase
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
    private val getProjectTasksAsFlowUseCase: GetProjectTasksAsFlowUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase
) : ViewModel() {


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])


    fun getProjectById() : Flow<ProjectEntity> = getProjectByIdAsFlowUseCase(projectId)

    fun getProjectTasks() : Flow<List<DoTooItemEntity>> = getProjectTasksAsFlowUseCase(projectId)

    fun getUserProfiles(ids : List<String>) : Flow<List<UserEntity>> = getUsersByIdsUseCase(ids)



    fun upsertTask(task: DoTooItemEntity, project : Project){
        when(getRole(project)){
            Roles.ProAdmin -> {
                updateTaskOnServer(task, project)
            }
            Roles.Admin -> {
                updateTaskLocally(task, project)
            }
            Roles.Editor -> {
                updateTaskOnServer(task, project)
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

    private fun updateTaskOnServer(task : DoTooItemEntity, project: Project){
        projectsReference
            .document(projectId)
            .collection("todos")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                updateProject(project)
            }
    }

    private fun updateTaskLocally(task : DoTooItemEntity, project : Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertDoToosUseCase(listOf(task.toDoTooItem()),projectId)
            updateProject(project)
        }
    }


    fun updateProject(project : Project){
        val projectCopy = project.copy()
        projectCopy.updatedAt = getSampleDateInLong()

        when(getRole(project)){
            Roles.ProAdmin -> {
                updateProjectOnSever(projectCopy)
            }
            Roles.Admin -> {
                updateProjectLocally(projectCopy)
            }
            Roles.Editor -> {
                updateProjectOnSever(projectCopy)
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


    private fun updateProjectOnSever(project : Project){
        projectsReference
            .document(projectId)
            .set(project)
    }

    private fun updateProjectLocally(project : Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertProjectUseCase(listOf(project))
        }
    }


    fun deleteProject(project: Project){

        when(getRole(project)){
            Roles.ProAdmin -> {
                deleteProjectOnServer()
            }
            Roles.Admin -> {
                deleteProjectLocally(project)
            }
            Roles.Editor -> {
                deleteProjectOnServer()
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

    private fun deleteProjectOnServer(){
        projectsReference
            .document(projectId)
            .delete()
    }

    private fun deleteProjectLocally(project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            deleteProjectUseCase(project = project)
        }
    }

    fun deleteTask(task : DoTooItemEntity, project: Project){
        when(getRole(project)){
            Roles.ProAdmin -> {
                deleteTaskOnServer(task, project)
            }
            Roles.Admin -> {
                deleteTaskLocally(task, project)
            }
            Roles.Editor -> {
                deleteTaskOnServer(task, project)
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
    private fun deleteTaskOnServer(task: DoTooItemEntity, project: Project){
        projectsReference
            .document(projectId)
            .collection("todos")
            .document(task.id)
            .delete()
            .addOnSuccessListener {
                updateProject(project)
            }
    }

    private fun deleteTaskLocally(task: DoTooItemEntity, project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoToosUseCase(task.toDoTooItem(), projectId = projectId)
            updateProject(project)
        }
    }


}