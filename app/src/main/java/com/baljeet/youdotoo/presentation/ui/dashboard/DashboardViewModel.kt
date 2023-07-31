package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    /**
     * fetch the all tasks from local database
     * **/
    fun allTasks(): Flow<List<DoTooItemEntity>> = getAllTasksUseCase()

}