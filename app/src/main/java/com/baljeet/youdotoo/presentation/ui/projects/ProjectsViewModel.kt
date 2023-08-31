package com.baljeet.youdotoo.presentation.ui.projects

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsWithDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectsWithDoToosUseCase: GetProjectsWithDoToosUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val deleteDoTooUseCase: DeleteDoTooUseCase
) : ViewModel() {

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")




    /**
     * Fetch all projects and there tasks from local db
     * Valid rule for both pro and non pro members
     * **/
    fun projectsWithTaskCount(): Flow<List<ProjectWithDoToos>> = getProjectsWithDoToosUseCase()



    fun createDummyProject(newProjectId: String) {
        val newProject = Project(
            id = newProjectId,
            name = "My tasks",
            description = "This is auto generated project, you can modify this project as per your wish.",
            ownerId = SharedPref.userId!!,
            collaboratorIds = listOf(),
            viewerIds = listOf(),
            update = "${SharedPref.userName} created this project named 'My tasks'.",
            color = getRandomColor(),
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

    fun updateTask(task: DoTooItemEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val projectEntity = getProjectByIdUseCase(projectId = task.projectId)
            updateTask(task,projectEntity.toProject())
        }
    }
    private fun updateTask(task: DoTooItemEntity, project : Project){
        when(getRole(project)){
            EnumRoles.ProAdmin -> {
                updateTaskOnServer(task, project)
            }
            EnumRoles.Admin -> {
                updateTaskLocally(task, project)
            }
            EnumRoles.Editor -> {
                updateTaskOnServer(task, project)
            }
            EnumRoles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            EnumRoles.Blocked -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
        }
    }

    private fun updateTaskOnServer(task : DoTooItemEntity, project: Project){
        projectsReference
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                updateProject(project)
            }

    }

    private fun updateTaskLocally(task : DoTooItemEntity, project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertDoToosUseCase(listOf(task.toDoTooItem()),task.projectId)
            updateProject(project)
        }
    }


    fun deleteTask(task : DoTooItemEntity){
        CoroutineScope(Dispatchers.IO).launch {
            val project = getProjectByIdUseCase(projectId = task.projectId)
            deleteTask(task, project)
        }
    }


    private fun deleteTask(task : DoTooItemEntity, projectEntity: ProjectEntity){
        val project = projectEntity.toProject()
        when(getRole(project)){
            EnumRoles.ProAdmin -> {
                deleteTaskOnServer(task, project)
            }
            EnumRoles.Admin -> {
                deleteTaskLocally(task, project)
            }
            EnumRoles.Editor -> {
                deleteTaskOnServer(task, project)
            }
            EnumRoles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            EnumRoles.Blocked -> {
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
            EnumRoles.ProAdmin -> {
                updateProjectOnSever(projectCopy)
            }
            EnumRoles.Admin -> {
                updateProjectLocally(projectCopy)
            }
            EnumRoles.Editor -> {
                updateProjectOnSever(projectCopy)
            }
            EnumRoles.Viewer -> {
                //Do nothing can't update anything
                //UI handles this by itself
            }
            EnumRoles.Blocked -> {
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