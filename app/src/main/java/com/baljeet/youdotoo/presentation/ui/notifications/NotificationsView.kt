package com.baljeet.youdotoo.presentation.ui.notifications

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.EditNotifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleInvitationNotification
import com.baljeet.youdotoo.common.getSampleMessageNotification
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.presentation.ui.notifications.components.NotificationItemView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooTextColor

@Composable
fun NotificationsView(
    notifications: List<NotificationEntity>,
    onNotificationClick : (NotificationEntity) -> Unit,
    onDeleteNotification : (NotificationEntity) -> Unit,
    onClearAll : () -> Unit,
    onClickSettings:() -> Unit,
    onClose: () -> Unit
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
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * Drawer close icon button
             * **/
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(40.dp)
                    )

            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = "Button to close side drawer.",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        LightDotooFooterTextColor
                    }
                )
            }

            /**
             * Showing user name
             * **/
            Text(
                text = "Notifications",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) {
                    NightDotooTextColor
                } else {
                    LightDotooFooterTextColor
                }
            )

            /**
             * Menu button to open Notifications
             * **/
            androidx.compose.material.IconButton(
                onClick = onClickSettings,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(40.dp)
                    )
            ) {
                androidx.compose.material.Icon(
                    Icons.Outlined.EditNotifications,
                    contentDescription = "Menu button to open notification settings",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        LightDotooFooterTextColor
                    }
                )
            }

        }

        LazyColumn(
            modifier = Modifier.weight(1f).padding(top = 20.dp)
        ){

            items(notifications){ notification ->
                NotificationItemView(
                    notification = notification,
                    onDeleteNotification = {

                    },
                    onItemClick = {

                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

        }

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewNotificationsView(){
    NotificationsView(
        notifications = listOf(
            getSampleMessageNotification(),
            getSampleInvitationNotification(),
            getSampleMessageNotification()
        ),
        onNotificationClick = {},
        onDeleteNotification = {},
        onClearAll = {},
        onClickSettings = {},
        onClose = {}
    )
}

