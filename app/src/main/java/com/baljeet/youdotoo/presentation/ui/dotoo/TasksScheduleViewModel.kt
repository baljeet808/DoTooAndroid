package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.domain.use_cases.doTooItems.GetAllTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksScheduleViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase
) : ViewModel() {

    fun getAllTasks()  = getAllTasksUseCase()

}