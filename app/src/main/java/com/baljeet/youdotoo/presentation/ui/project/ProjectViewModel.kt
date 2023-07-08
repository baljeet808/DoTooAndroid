package com.baljeet.youdotoo.presentation.ui.project

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithEveryThing
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetProjectDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.DeleteProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.users.UpsertUserUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val upsertUserUseCase: UpsertUserUseCase,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val getProjectDoToosUseCase: GetProjectDoToosUseCase
) : ViewModel() {

    var projectState = mutableStateOf(ProjectWithEveryThing())
        private set

    private var db = Firebase.firestore

    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])

    init {
        if (SharedPref.isUserAPro) {
            projectsReference.document(projectId).addSnapshotListener { snapshot, error ->
                if (snapshot != null && error == null) {
                    val project = Project(
                        id = snapshot.getString("id") ?: "",
                        name = snapshot.getString("name") ?: "",
                        description = snapshot.getString("description") ?: "",
                        ownerId = snapshot.getString("ownerId") ?: "",
                        viewerIds = (snapshot.get("viewerIds") as List<String>),
                        collaboratorIds = (snapshot.get("collaboratorIds") as List<String>),
                        update = snapshot.getString("update") ?: ""
                    )
                    //saving to local database
                    viewModelScope.launch {
                        upsertProjectUseCase(listOf(project))
                    }
                    fetchProjectTodosOnline(project)
                }
            }
        } else {
            viewModelScope.launch {
                val project = getProjectByIdUseCase(projectId)
                val tasks = getProjectDoToosUseCase(projectId = project.id)

                if (isProjectIsSharedToUser(project)) {
                    fetchProfilesForProjects(project = project, todos = tasks)
                } else {
                    projectState.value = projectState.value.copy(
                        project = project,
                        doToos = tasks
                    )
                }
            }
        }

    }

    private fun isProjectIsSharedToUser(project: Project) =
        project.collaboratorIds.contains(SharedPref.userId) ||
    project.viewerIds.contains(SharedPref.userId)



    private fun fetchProjectTodosOnline(project: Project) {
        projectsReference
            .document(project.id)
            .collection("todos")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null && error == null) {
                    val doToos = ArrayList<DoTooItem>()
                    for (doToo in snapshot) {
                        doToos.add(
                            DoTooItem(
                                id = doToo.getString("id") ?: "",
                                title = doToo.getString("title") ?: "",
                                description = doToo.getString("description") ?: "",
                                createDate = doToo.getLong("createDate") ?: 0L,
                                dueDate = doToo.getLong("dueDate") ?: 0L,
                                priority = doToo.getString("priority") ?: "High",
                                updatedBy = doToo.getString("updatedBy") ?: "",
                                done = doToo.getBoolean("done") ?: false
                            )
                        )
                    }
                    viewModelScope.launch {
                        upsertDoToosUseCase(doToos,projectId)
                    }
                    fetchProfilesForProjects(project,doToos)
                } else if(snapshot == null && error == null) {
                    projectState.value = projectState.value.copy(
                        project = project,
                        doToos = listOf()
                    )
                }else{
                    projectState.value = projectState.value.copy(
                        project = null,
                        doToos = listOf()
                    )
                }
            }
    }


    private fun fetchProfilesForProjects(project: Project, todos: List<DoTooItem>) {
        val profiles = arrayListOf<User>()
        val allIds = arrayListOf<String>()

        allIds.addAll(project.collaboratorIds)
        allIds.addAll(project.viewerIds)

        viewModelScope.launch {
            allIds.forEach {userId ->
                val localUser = getUserByIdUseCase(userId)
                if(localUser != null){
                    profiles.add(localUser)
                }else{
                    db.collection("users").whereEqualTo("id", userId).get()
                        .addOnSuccessListener { documents ->
                            documents.first()?.let { data ->
                                val onlineUser = User(
                                    id = data.getString("id") ?: "",
                                    name = data.getString("name") ?: "",
                                    email = data.getString("description") ?: "",
                                    avatarUrl = data.getString("avatarUrl") ?: "",
                                    joined = data.getLong("joined") ?: 0L
                                )
                                profiles.add(
                                    onlineUser
                                )
                                viewModelScope.launch {
                                    upsertUserUseCase(listOf(onlineUser))
                                }
                            }
                        }.await()
                }
            }
            projectState.value = projectState.value.copy(
                project = project,
                doToos = todos,
                profiles = profiles
            )
        }
    }

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
            viewModelScope.launch {
                upsertDoToosUseCase(listOf(newDoToo),projectId)
            }
        }else {
            viewModelScope.launch {
                upsertDoToosUseCase(listOf(newDoToo),projectId)
            }
        }
    }
}