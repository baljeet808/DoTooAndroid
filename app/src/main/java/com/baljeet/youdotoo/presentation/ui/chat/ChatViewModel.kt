package com.baljeet.youdotoo.presentation.ui.chat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.baljeet.youdotoo.common.getUserIds
import com.baljeet.youdotoo.common.updateInteraction
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.messages.GetPagedMessagesOfProject
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectByIdAsFlowUseCase
import com.baljeet.youdotoo.domain.use_cases.users.GetUsersByIdsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Updated by Baljeet singh on 23rd June, 2023 at 5:15PM.
 * **/
@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProjectByIdAsFlowUseCase: GetProjectByIdAsFlowUseCase,
    private val getUsersByIdsUseCase: GetUsersByIdsUseCase,
    private val getPagedMessagesOfProject: GetPagedMessagesOfProject
) : ViewModel() {



    private val projectId: String = checkNotNull(savedStateHandle["projectId"])


    private var chatRef = FirebaseFirestore.getInstance().collection("projects")


    fun getProjectById(): Flow<ProjectEntity> = getProjectByIdAsFlowUseCase(projectId)


    fun getUsersOFProject( project : Project) = getUsersByIdsUseCase(project.getUserIds())



    fun getAllMessagesOfThisProject() : Flow<PagingData<MessageEntity>> = getPagedMessagesOfProject(projectId)
        .cachedIn(viewModelScope)


    fun interactWithMessage(
        message: MessageEntity,
        emoticon: String
    ) {
        val newMessage = message.copy()
        newMessage.updateInteraction(emoticon)
        chatRef
            .document(projectId)
            .collection("messages")
            .document(message.id)
            .set(newMessage)
    }


}