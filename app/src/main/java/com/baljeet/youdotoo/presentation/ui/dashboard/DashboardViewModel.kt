package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    /**
     * fetch the all tasks from local database
     * **/
    fun allTasks(): Flow<List<TaskEntity>> = getAllTasksUseCase()

    fun signOut(){
        firebaseAuth.signOut()
    }

}