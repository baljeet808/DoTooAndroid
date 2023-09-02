package com.baljeet.youdotoo.presentation.ui.create_task

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getExactDateTimeInSecondsFrom1970
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDateInLong
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetAllProjectsAsFlowUseCase
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
    private val getProjectsUseCase: GetAllProjectsAsFlowUseCase
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

        val newTask = TaskEntity(
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
            projectId = selectedProject.id
        )
        createTask(newTask, selectedProject)
    }

    private fun createTask(task: TaskEntity, project : Project){
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

    private fun updateTaskOnServer(task : TaskEntity, project : Project){
        projectsReference
            .document(project.id)
            .collection("todos")
            .document(task.id)
            .set(task)
            .addOnSuccessListener {
                updateProject(project)
            }
    }

    private fun updateTaskLocally(task : TaskEntity, project: Project){
        CoroutineScope(Dispatchers.IO).launch {
            upsertDoToosUseCase(listOf(task))
            updateProject(project)
        }
    }


    private fun updateProject(project : Project){
        val projectCopy = project.copy()
        projectCopy.updatedAt = getSampleDateInLong()

        when(getRole(project)){
            EnumRoles.ProAdmin -> {
                updateProjectOnSever(project)
            }
            EnumRoles.Admin -> {
                updateProjectLocally(project)
            }
            EnumRoles.Editor -> {
                updateProjectOnSever(project)
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