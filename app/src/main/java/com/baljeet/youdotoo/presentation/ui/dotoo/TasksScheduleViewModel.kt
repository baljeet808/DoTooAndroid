package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetDoTooByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksScheduleViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getDoTooByIdUseCase: GetDoTooByIdUseCase,
    private val deleteDoTooUseCase: DeleteDoTooUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase
) : ViewModel() {

    fun getAllTasks()  = getAllTasksUseCase()


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")


    fun deleteTask(task : DoTooItemEntity){
        CoroutineScope(Dispatchers.IO).launch {
            val taskEntity = getDoTooByIdUseCase(task.id)
            val project = getProjectByIdUseCase(projectId = taskEntity.projectId)
            deleteTask(taskEntity, project)
        }
    }


    private fun deleteTask(task : DoTooItemEntity, projectEntity: ProjectEntity){
        val project = projectEntity.toProject()
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
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .delete()
            .addOnSuccessListener {
                updateProject(project)
            }
    }

    private fun deleteTaskLocally(task: DoTooItemEntity, project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoTooUseCase(task.toDoTooItem(), projectId = task.projectId)
            updateProject(project)
        }
    }


    private fun updateProject(project : Project){
        val projectCopy = project.copy()
        projectCopy.updatedAt = getSampleDateInLong()

        when(getRole(project)){
            Roles.ProAdmin -> {
                updateProjectOnSever(project)
            }
            Roles.Admin -> {
                updateProjectLocally(project)
            }
            Roles.Editor -> {
                updateProjectOnSever(project)
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
            .document(project.id)
            .set(project)
    }

    private fun updateProjectLocally(project : Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertProjectUseCase(listOf(project))
        }
    }

}