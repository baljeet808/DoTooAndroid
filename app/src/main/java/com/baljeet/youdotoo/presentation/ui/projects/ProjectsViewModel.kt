package com.baljeet.youdotoo.presentation.ui.projects

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllOtherDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetDoTooByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetPendingDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetTodayDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetTomorrowDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetYesterdayDoToosUseCase
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
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject


@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectsWithDoToosUseCase: GetProjectsWithDoToosUseCase,
    private val getDoTooByIdUseCase: GetDoTooByIdUseCase,
    private val deleteDoToosUseCase: DeleteDoTooUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val getYesterdayDoToosUseCase: GetYesterdayDoToosUseCase,
    private val getTodayDoToosUseCase: GetTodayDoToosUseCase,
    private val getTomorrowDoToosUseCase: GetTomorrowDoToosUseCase,
    private val getPendingDoToosUseCase: GetPendingDoToosUseCase,
    private val getAllOtherDoToosUseCase: GetAllOtherDoToosUseCase,
) : ViewModel() {

    private val todayDate = java.time.LocalDateTime.now().toKotlinLocalDateTime()

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val todayDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault()).epochSeconds


    private val tomorrowDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault())
        .plus(
            unit = DateTimeUnit.DAY,
            value = 1,
            timeZone = TimeZone.currentSystemDefault()
        ).epochSeconds

    private val yesterdayDateInLong = LocalDateTime(
        year = todayDate.year,
        monthNumber = todayDate.monthNumber,
        dayOfMonth = todayDate.dayOfMonth,
        hour = 9,
        minute = 0,
        second = 0
    ).toInstant(TimeZone.currentSystemDefault())
        .minus(
            unit = DateTimeUnit.DAY,
            value = 1,
            timeZone = TimeZone.currentSystemDefault()
        ).epochSeconds



    /**
     * Fetch all projects and there tasks from local db
     * Valid rule for both pro and non pro members
     * **/
    fun projectsWithTaskCount(): Flow<List<ProjectWithDoToos>> = getProjectsWithDoToosUseCase()

    /**
     * fetch the yesterday's tasks from local database only and
     * then update the firestore when we make any changes to them
     * **/
    fun yesterdayTasks(): Flow<List<DoTooItemEntity>> = getYesterdayDoToosUseCase(yesterdayDateInLong)

    /**
     * fetch the today's tasks from local database only and
     * then update the firestore when we make any changes to them
     * **/
    fun todayTasks(): Flow<List<DoTooItemEntity>> = getTodayDoToosUseCase(todayDateInLong)

    /**
     * fetch the tomorrow's tasks from local database only and
     * then update the firestore when we make any changes to them
     * **/
    fun tomorrowTasks(): Flow<List<DoTooItemEntity>> = getTomorrowDoToosUseCase(tomorrowDateInLong)

    /**
     * fetch the pending tasks of past from local database only and
     * then update the firestore when we make any changes to them
     * **/
    fun pendingTasks(): Flow<List<DoTooItemEntity>> = getPendingDoToosUseCase(yesterdayDateInLong)

    /**
     * fetch all other tasks of future from local database only and
     * then update the firestore when we make any changes to them
     * **/
    fun allOtherTasks(): Flow<List<DoTooItemEntity>> = getAllOtherDoToosUseCase(tomorrowDateInLong)


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

    fun updateTask(task: DoTooItem) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskEntity = getDoTooByIdUseCase(task.id)
            val projectEntity = getProjectByIdUseCase(projectId = taskEntity.projectId)
            updateTask(taskEntity,projectEntity.toProject())
        }
    }
    private fun updateTask(task: DoTooItemEntity, project : Project){
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



    fun deleteTask(task : DoTooItem){
        CoroutineScope(Dispatchers.IO).launch {
            val taskEntity = getDoTooByIdUseCase(task.id)
            val project = getProjectByIdUseCase(projectId = taskEntity.projectId)
            deleteTask(taskEntity, project)
        }
    }


    private fun deleteTask(task : DoTooItemEntity, project: ProjectEntity){
        when(getRole(project.toProject())){
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
    private fun deleteTaskOnServer(task: DoTooItemEntity, project : ProjectEntity){
        projectsReference
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .delete()
            .addOnSuccessListener {
                updateProject(project.toProject())
            }
    }

    private fun deleteTaskLocally(task: DoTooItemEntity , project : ProjectEntity){
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoToosUseCase(task.toDoTooItem(), projectId = task.projectId)
            updateProject(project.toProject())
        }
    }


}