package com.baljeet.youdotoo.presentation.ui.attachment_viewer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.domain.use_cases.messages.GetMessageByIdAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AttachmentViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMessageByIdAsFlowUseCase: GetMessageByIdAsFlowUseCase
) : ViewModel() {

    val messageId : String = checkNotNull(savedStateHandle["messageId"])

    fun getMessageAsFlow() = getMessageByIdAsFlowUseCase(messageId)

}