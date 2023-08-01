package com.baljeet.youdotoo.presentation.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue

@Composable
fun NotificationsView(
    notifications: List<NotificationEntity>,
    onNotificationClick : (NotificationEntity) -> Unit,
    onDeleteNotification : (NotificationEntity) -> Unit,
    onClearAll : () -> Unit,
    onClickSettings:() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooNormalBlue
                } else {
                    DotooGray
                }
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = "Notifications")

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNotificationsView(){
    NotificationsView(
        notifications = listOf(),
        onNotificationClick = {},
        onDeleteNotification = {},
        onClearAll = {},
        onClickSettings = {}
    )
}

