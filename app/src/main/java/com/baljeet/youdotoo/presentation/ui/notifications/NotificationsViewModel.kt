package com.baljeet.youdotoo.presentation.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.domain.use_cases.notifications.DeleteNotificationUseCase
import com.baljeet.youdotoo.domain.use_cases.notifications.GetAllNotificationsAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val getAllNotificationsUseCase: GetAllNotificationsAsFlowUseCase,
    private val deleteNotificationUseCase: DeleteNotificationUseCase
) : ViewModel() {

    fun getAllNotificationsAsFlow() = getAllNotificationsUseCase()

    fun deleteNotification(notification : NotificationEntity){
        viewModelScope.launch {
            deleteNotificationUseCase(notification)
        }
    }


}