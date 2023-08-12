package com.baljeet.youdotoo.presentation.ui.profile_quick_view

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileQuickViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserByIdAsFlowUseCase: GetUserByIdAsFlowUseCase
) : ViewModel() {

    val userId : String = checkNotNull(savedStateHandle["userId"])

    fun getUserAsFlow() = getUserByIdAsFlowUseCase(userId)

}