package com.baljeet.youdotoo.presentation.ui.edittask

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getExactDateTimeInSecondsFrom1970
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetTaskByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdAsFlowUseCase: GetTaskByIdAsFlowUseCase,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase
) : ViewModel() {

    private val onlineDB = FirebaseFirestore.getInstance()

    fun getTaskByIdAsFlow(taskId : String) = getTaskByIdAsFlowUseCase(taskId)

    fun getProject(projectId : String) = getProjectByIdAsFlowUseCase(projectId)

    fun updateTask(
        task: DoTooItemEntity,
        name : String,
        description : String,
        priority: Priorities,
        dueDate: DueDates,
        customDate : LocalDate?,
        selectedProject : Project
    ){
        val updatedTask = task.copy(
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
            updatedBy = SharedPref.userName.plus(" updated this Task."),
            projectColor = selectedProject.color
        )
        onlineDB.collection("projects")
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .set(updatedTask)
    }

}