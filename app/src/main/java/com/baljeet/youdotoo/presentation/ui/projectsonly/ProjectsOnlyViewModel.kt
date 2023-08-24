package com.baljeet.youdotoo.presentation.ui.projectsonly

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsWithDoToosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectsOnlyViewModel @Inject constructor(
    private val getProjectsWithDoToosUseCase: GetProjectsWithDoToosUseCase
) : ViewModel() {


    fun getProjectsWithTasks() = getProjectsWithDoToosUseCase()

}