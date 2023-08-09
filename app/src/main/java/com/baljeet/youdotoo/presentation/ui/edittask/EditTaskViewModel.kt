package com.baljeet.youdotoo.presentation.ui.edittask

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetTaskByIdAsFlowUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val getTaskByIdAsFlowUseCase: GetTaskByIdAsFlowUseCase
) : ViewModel() {

    private val onlineDB = FirebaseFirestore.getInstance()

    fun getTaskByIdAsFlow(taskId : String) = getTaskByIdAsFlowUseCase(taskId)

    fun updateTask(task : DoTooItemEntity, title : String){
        val updatedTask = task.copy(
            title = title
        )
        onlineDB.collection("projects")
            .document(task.projectId)
            .collection("todos")
            .document(task.id)
            .set(updatedTask)
    }

}