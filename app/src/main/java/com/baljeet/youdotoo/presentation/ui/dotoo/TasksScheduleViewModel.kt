package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
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
            if(SharedPref.isUserAPro){
                projectsReference
                    .document(project.id)
                    .collection("todos")
                    .document(task.id)
                    .delete()
            }
            deleteDoTooUseCase(task, projectId = project.id)
        }

    }

}