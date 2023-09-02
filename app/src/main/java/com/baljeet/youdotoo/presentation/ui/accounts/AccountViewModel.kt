package com.baljeet.youdotoo.presentation.ui.accounts

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.use_cases.users.GetUserByIdAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getUserByIdAsFlowUseCase: GetUserByIdAsFlowUseCase
): ViewModel() {

    fun userAsFlow() = getUserByIdAsFlowUseCase(SharedPref.userId!!)

}