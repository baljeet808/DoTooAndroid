package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.Roles
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItem
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.use_cases.doTooItems.DeleteDoTooUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetDoTooByIdUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdUseCase
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
    private val deleteDoTooUseCase: DeleteDoTooUseCase
) : ViewModel() {

    fun getAllTasks()  = getAllTasksUseCase()


    private var projectsReference = FirebaseFirestore
        .getInstance()
        .collection("projects")


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
                deleteTaskOnServerAndLocally(task)
            }
            Roles.Admin -> {
                deleteTaskOnServerAndLocally(task)
            }
            Roles.Editor -> {
                deleteTaskOnServerAndLocally(task)
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
    private fun deleteTaskOnServerAndLocally(task: DoTooItemEntity){
        projectsReference
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .delete()
            .addOnCompleteListener {
                deleteTaskLocally(task)
            }
    }

    private fun deleteTaskLocally(task: DoTooItemEntity){
        CoroutineScope(Dispatchers.IO).launch {
            deleteDoTooUseCase(task.toDoTooItem(), projectId = task.projectId)
        }
    }



}