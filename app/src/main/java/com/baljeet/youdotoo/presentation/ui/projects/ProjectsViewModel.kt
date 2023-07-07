package com.baljeet.youdotoo.presentation.ui.projects

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.converters.convertLocalDateTimeToEpochSeconds
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetTodayDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.DeleteProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.SearchProjectsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

data class ProjectWithTaskCount(
    var project : Project,
    var taskCount : Int,
    var progress : Float
)

data class ProjectsState(
    var onlineProjects: List<ProjectWithTaskCount> = listOf(),
    var offlineProjects: List<ProjectWithTaskCount> = listOf(),
    var todayTasks: List<DoTooItem> = listOf()
)

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectsUseCase: GetProjectsUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val searchProjectsUseCase: SearchProjectsUseCase,
    private val getTodayDoToosUseCase: GetTodayDoToosUseCase,
    private val getProjectDoToosUseCase: GetProjectDoToosUseCase
) : ViewModel() {

    var projectState = mutableStateOf(ProjectsState())
        private set

    private val todayDateInLong = java.time.LocalDateTime.now().toKotlinLocalDateTime()
    private val todayStartDateTime = LocalDateTime(
        year = todayDateInLong.year,
        monthNumber = todayDateInLong.monthNumber,
        dayOfMonth = todayDateInLong.dayOfMonth,
        hour = 0,
        minute = 0
    )
    private val todayEndDateTime = LocalDateTime(
        year = todayDateInLong.year,
        monthNumber = todayDateInLong.monthNumber,
        dayOfMonth = todayDateInLong.dayOfMonth,
        hour = 23,
        minute = 59,
        second = 59
    )


    private var db = Firebase.firestore

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private var projectsQuery = projectsReference
        .where(
            Filter.or(
                Filter.arrayContains("collaboratorIds", SharedPref.userId),
                Filter.arrayContains("viewerIds", SharedPref.userId)
            )
        )


    init {
        if (SharedPref.isUserAPro) {
            projectsQuery = projectsReference.where(
                Filter.or(
                    Filter.equalTo("ownerId", SharedPref.userId),
                    Filter.arrayContains("collaboratorIds", SharedPref.userId),
                    Filter.arrayContains("viewerIds", SharedPref.userId)
                )
            )
        }

        if (SharedPref.isUserAPro.not()) {
            viewModelScope.launch {
                val offlineProjects = getProjectsUseCase().toCollection(ArrayList())
                val projects = arrayListOf<ProjectWithTaskCount>()
                offlineProjects.forEach { project ->
                    val tasks = getProjectDoToosUseCase(projectId = project.id)
                    projects.add(
                        ProjectWithTaskCount(
                            project = project,
                            taskCount = tasks.count(),
                            progress = tasks.count { task -> task.done }.toFloat() / tasks.count().toFloat()
                        )
                    )
                }
                projectState.value = projectState.value.copy(
                    offlineProjects = projects
                )
            }
        }

        projectsQuery.addSnapshotListener { snapshot, error ->
            if (snapshot != null && error == null) {
                val onlineProjects = arrayListOf<Project>()
                for (project in snapshot) {
                    onlineProjects.add(
                        Project(
                            id = project.getString("id") ?: "",
                            name = project.getString("name") ?: "",
                            description = project.getString("description") ?: "",
                            ownerId = project.getString("ownerId") ?: "",
                            viewerIds = (project.get("viewerIds") as List<String>),
                            collaboratorIds = (project.get("collaboratorIds") as List<String>),
                            update = project.getString("update") ?: ""
                        )
                    )
                }

                viewModelScope.launch {
                    val projects = arrayListOf<ProjectWithTaskCount>()
                    onlineProjects.forEach { project ->
                        val tasks = getProjectDoToosUseCase(projectId = project.id)
                        projects.add(
                            ProjectWithTaskCount(
                                project = project,
                                taskCount = tasks.count(),
                                progress = tasks.count { task -> task.done }.toFloat() / tasks.count().toFloat()
                            )
                        )
                    }
                    // save the user owned projects to local db
                    viewModelScope.launch {
                        upsertProjectUseCase.invoke(onlineProjects.filter { project -> project.ownerId == SharedPref.userId })
                    }
                    projectState.value = projectState.value.copy(
                        onlineProjects = projects
                    )
                }
            }
        }


        /**
         * fetch the today's tasks from local database only and
         * then update the firestore when we make any changes to them
         * **/
        viewModelScope.launch {
            projectState.value = projectState.value.copy(
                todayTasks = getTodayDoToosUseCase(
                    startTimeDate = convertLocalDateTimeToEpochSeconds(todayStartDateTime),
                    endTimeDate = convertLocalDateTimeToEpochSeconds(todayEndDateTime)
                )
            )
        }

    }


    private fun fetchProfilesForProjects(projects: List<Project>) {
        val projectsWithProfiles = arrayListOf<ProjectWithProfiles>()
        viewModelScope.launch {
            for (project in projects) {
                val profiles = getUserProfiles(project)
                projectsWithProfiles.add(
                    ProjectWithProfiles(
                        profiles = profiles.ifEmpty { null },
                        project = project
                    )
                )
            }
        }
    }

    suspend fun getUserProfiles(project: Project): ArrayList<User> {

        val ids = if (project.collaboratorIds.isNotEmpty()) {
            project.collaboratorIds.toCollection(ArrayList())
        } else {
            arrayListOf()
        }
        if (project.viewerIds.isNotEmpty()) {
            ids.addAll(project.viewerIds)
        }
        ids.add(SharedPref.userId!!)
        val users = ArrayList<User>()
        for (userId in ids) {
            db.collection("users").whereEqualTo("id", userId).get()
                .addOnSuccessListener { documents ->
                    documents.first()?.let { data ->
                        users.add(
                            User(
                                id = data.getString("id") ?: "",
                                name = data.getString("name") ?: "",
                                email = data.getString("description") ?: "",
                                avatarUrl = data.getString("avatarUrl") ?: "",
                                joined = data.getLong("joined") ?: 0L
                            )
                        )
                    }
                }.await()
        }
        return users
    }
}