package com.baljeet.youdotoo.presentation.ui.create_task

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getExactDateTimeInSecondsFrom1970
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsUseCase
import com.baljeet.youdotoo.domain.use_cases.project.UpsertProjectUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CreateTaskViewModel  @Inject constructor(
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val getProjectsUseCase: GetProjectsUseCase
) : ViewModel() {


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")

    fun getProjects() = getProjectsUseCase()

    fun createTask(
        name : String,
        description : String,
        priority: Priorities,
        dueDate: DueDates,
        customDate : LocalDate?,
        selectedProject : Project
    ){

        val newTask = DoTooItem(
            id = UUID.randomUUID().toString(),
            title = name,
            description = description,
            priority = priority.toString,
            dueDate = when (dueDate) {
                DueDates.CUSTOM -> {
                    customDate?.getExactDateTimeInSecondsFrom1970()?:0L
                }
                else -> {
                    dueDate.getExactDate().getExactDateTimeInSecondsFrom1970()
                }
            },
            createDate = java.time.LocalDateTime.now().toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            updatedBy = SharedPref.userName.plus(" created this Task."),
            done = false,
            projectColor = selectedProject.color
        )
        createTask(newTask, selectedProject)
    }

    private fun createTask(task: DoTooItem, project : Project){
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

    private fun updateTaskOnServer(task : DoTooItem, project : Project){
        projectsReference
            .document(project.id)
            .collection("todos")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                updateProject(project)
            }
    }

    private fun updateTaskLocally(task : DoTooItem, project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertDoToosUseCase(listOf(task),project.id)
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