package com.baljeet.youdotoo.presentation.ui.projects

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
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



    private fun isProjectIsSharedToUser(project: ProjectEntity) =
        project.ownerId != SharedPref.userId


    fun upsertDoToo(doTooItem: DoTooItem) {
        val newDoToo = doTooItem.copy()
        newDoToo.done = doTooItem.done.not()
        newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ")
            .plus(if (newDoToo.done) "completed." else "not completed.")

        CoroutineScope(Dispatchers.IO).launch {
            val task = getDoTooByIdUseCase(doTooItem.id)
            val project = getProjectByIdUseCase(projectId = task.projectId)
            if (SharedPref.isUserAPro || isProjectIsSharedToUser(project)) {
                projectsReference
                    .document(project.id)
                    .collection("todos")
                    .document(newDoToo.id)
                    .set(newDoToo)

            }else{
                upsertDoToosUseCase(listOf(newDoToo), project.id)
            }
            upsertProject(project)
        }
    }


    fun updateTaskTitle(doTooItem: DoTooItem, title : String) {
        val newDoToo = doTooItem.copy(
            title = title
        )
        newDoToo.updatedBy = SharedPref.userName.plus(" has updated task title.")

        CoroutineScope(Dispatchers.IO).launch {
            val task = getDoTooByIdUseCase(doTooItem.id)
            val project = getProjectByIdUseCase(projectId = task.projectId)
            if (SharedPref.isUserAPro || isProjectIsSharedToUser(project)) {
                projectsReference
                    .document(project.id)
                    .collection("todos")
                    .document(newDoToo.id)
                    .set(newDoToo)

            }else{
                upsertDoToosUseCase(listOf(newDoToo), project.id)
            }
            upsertProject(project)
        }
    }


    private fun upsertProject(project : ProjectEntity){
        val newProject = project.copy()
        newProject.updatedAt = getSampleDateInLong()
        if(SharedPref.isUserAPro || isProjectIsSharedToUser(project)){
            projectsReference
                .document(project.id)
                .set(newProject.toProject())
        }else{
            CoroutineScope(Dispatchers.IO).launch {
                upsertProjectUseCase(listOf(newProject.toProject()))
            }
        }
    }




    fun createDummyProject(newProjectId: String) {
        val newProject = Project(
            id = newProjectId,
            name = "My tasks",
            description = "This is auto generated project, you can modify this project as per your wish.",
            ownerId = SharedPref.userId!!,
            collaboratorIds = listOf(),
            viewerIds = listOf(),
            update = "${SharedPref.userName} created this Project named 'My tasks'.",
            color = getRandomColor(),
            updatedAt = getSampleDateInLong()
        )
        CoroutineScope(Dispatchers.IO).launch {
            if (SharedPref.isUserAPro) {
                projectsReference
                    .document(newProjectId)
                    .set(newProject)
            } else {
                upsertProjectUseCase(listOf(newProject))
            }
        }
    }


    fun deleteTask(task : DoTooItem){
       CoroutineScope(Dispatchers.IO).launch {
           val taskEntity = getDoTooByIdUseCase(task.id)
           val project = getProjectByIdUseCase(projectId = taskEntity.projectId)
           if(SharedPref.isUserAPro){
               projectsReference
                   .document(project.id)
                   .collection("todos")
                   .document(task.id)
                   .delete()
           }
           deleteDoToosUseCase(task, projectId = project.id)
       }

    }


}