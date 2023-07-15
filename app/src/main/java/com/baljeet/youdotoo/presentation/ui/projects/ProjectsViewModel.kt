package com.baljeet.youdotoo.presentation.ui.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.doTooItems.*
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsWithDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.users.UpsertUserUseCase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.*
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
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val upsertUserUseCase: UpsertUserUseCase
) : ViewModel() {

    private val todayDate = java.time.LocalDateTime.now().toKotlinLocalDateTime()


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


    private var db = Firebase.firestore

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private var projectsQuery: Query = projectsReference


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


    init {

        //online db connection

        /**
         * if user is pro change query to fetch all projects from firestore
         * else only fetch the projects which are 'shared to' user from other users
         * **/
        if (SharedPref.isUserAPro) {
            projectsQuery = projectsReference.where(
                Filter.or(
                    Filter.equalTo("ownerId", SharedPref.userId),
                    Filter.arrayContains("collaboratorIds", SharedPref.userId),
                    Filter.arrayContains("viewerIds", SharedPref.userId)
                )
            )
        } else {
            projectsQuery = projectsReference.where(
                Filter.or(
                    Filter.arrayContains("collaboratorIds", SharedPref.userId),
                    Filter.arrayContains("viewerIds", SharedPref.userId)
                )
            )
        }

        /**
         * Connect user to firestore online projects
         * and their tasks. and save/update everything to local db
         * **/
        projectsQuery.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {
                for (project in snapshot) {
                    val onlineProject = Project(
                        id = project.getString("id") ?: "",
                        name = project.getString("name") ?: "",
                        description = project.getString("description") ?: "",
                        ownerId = project.getString("ownerId") ?: "",
                        viewerIds = (project.get("viewerIds") as List<String>),
                        collaboratorIds = (project.get("collaboratorIds") as List<String>),
                        update = project.getString("update") ?: "",
                        color = project.getLong("color") ?: 4278215265
                    )

                    // save all online projects to local db
                    viewModelScope.launch {
                        upsertProjectUseCase.invoke(arrayListOf(onlineProject))
                    }

                    projectsReference
                        .document(onlineProject.id)
                        .collection("todos")
                        .addSnapshotListener { sShot, e ->
                            if (sShot != null && e == null) {
                                val doToos = ArrayList<DoTooItem>()
                                for (doToo in sShot) {
                                    doToos.add(
                                        DoTooItem(
                                            id = doToo.getString("id") ?: "",
                                            title = doToo.getString("title") ?: "",
                                            description = doToo.getString("description") ?: "",
                                            createDate = doToo.getLong("createDate") ?: 0L,
                                            dueDate = doToo.getLong("dueDate") ?: 0L,
                                            priority = doToo.getString("priority") ?: "High",
                                            updatedBy = doToo.getString("updatedBy") ?: "",
                                            done = doToo.getBoolean("done") ?: false,
                                            projectColor = doToo.getLong("projectColor") ?: getRandomColor(),
                                        )
                                    )
                                }
                                viewModelScope.launch {
                                    upsertDoToosUseCase(
                                        dotoos = doToos,
                                        projectId = onlineProject.id
                                    )
                                }
                            }
                        }
                    getUserProfilesAndUpdateDatabase(onlineProject)
                }
            }
        }

    }


    private fun getUserProfilesAndUpdateDatabase(project: Project) {

        val ids = project.getUserIds()

        viewModelScope.launch {
            ids.forEach { userId ->
                //only fetch from firestore if user is not in the local DB
                getUserByIdUseCase(userId) ?: kotlin.run {
                    db.collection("users").whereEqualTo("id", userId).get()
                        .addOnSuccessListener { documents ->
                            documents.first()?.let { data ->
                                val user = User(
                                    id = data.getString("id") ?: "",
                                    name = data.getString("name") ?: "",
                                    email = data.getString("description") ?: "",
                                    avatarUrl = data.getString("avatarUrl") ?: "",
                                    joined = data.getLong("joined") ?: 0L
                                )
                                //save user in local db
                                viewModelScope.launch {
                                    upsertUserUseCase(listOf(user))
                                }
                            }
                        }.await()
                }
            }
        }

    }


    private fun isProjectIsSharedToUser(project: ProjectEntity) =
        project.ownerId != SharedPref.userId


    fun upsertDoToo(doTooItem: DoTooItem) {
        val newDoToo = doTooItem.copy()
        newDoToo.done = doTooItem.done.not()
        newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ")
            .plus(if (newDoToo.done) "completed." else "not completed.")

        viewModelScope.launch {
            val task = getDoTooByIdUseCase(doTooItem.id)
            val project = getProjectByIdUseCase(projectId = task.projectId)
            if (SharedPref.isUserAPro || isProjectIsSharedToUser(project)) {
                projectsReference
                    .document(project.id)
                    .collection("todos")
                    .document(newDoToo.id)
                    .set(newDoToo)

            }
            upsertDoToosUseCase(listOf(newDoToo), project.id)
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
            color = getRandomColor()
        )
        viewModelScope.launch {
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
       viewModelScope.launch {
           val taskEntity = getDoTooByIdUseCase(task.id)
           val project = getProjectByIdUseCase(projectId = taskEntity.projectId)
           if(SharedPref.isUserAPro){
               projectsReference
                   .document(project.id)
                   .collection("todos")
                   .document(task.id)
                   .delete()
           }else{
               deleteDoToosUseCase(task, projectId = project.id)
           }
       }

    }


}