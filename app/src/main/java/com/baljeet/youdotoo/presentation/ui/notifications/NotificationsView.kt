package com.baljeet.youdotoo.presentation.ui.notifications

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNotifications
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleInvitationNotification
import com.baljeet.youdotoo.common.getSampleMessageNotification
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.presentation.ui.notifications.components.NotificationItemView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun NotificationsView(
    notifications: List<NotificationEntity>,
    onNotificationClick : (NotificationEntity) -> Unit,
    onDeleteNotification : (NotificationEntity) -> Unit,
    onClearAll : () -> Unit,
    onClickSettings:() -> Unit,
    onClose: () -> Unit
) {

    val lazyListState  = rememberLazyListState()

    val deleteScope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = getLightThemeColor()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.End
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * Close icon button
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
                    contentDescription = "Button to close current screen.",
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

            /**
             * Title
             * **/
            Text(
                text = "Notifications",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
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
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

        }

        AnimatedVisibility(visible = lazyListState.isScrolled.not()) {
            /**
             * Clear all notifications button
             * **/
            TextButton(
                onClick = onClearAll,
                modifier = Modifier

            ) {
                Text(
                    text = "Clear all",
                    modifier = Modifier,
                    textAlign = TextAlign.End,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    textDecoration = TextDecoration.Underline
                )
            }

        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .weight(1f)
                .padding(top = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){

            items(notifications, key = {it.id}){ notification ->


                val state = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            deleteScope.launch {
                                delay(1000)
                                onDeleteNotification(notification)
                            }
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    modifier = Modifier.animateItemPlacement(),
                    state = state,
                    background = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(start = 20.dp, end = 20.dp)
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                androidx.compose.material.Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Deleted notification icon",
                                    tint = LightAppBarIconsColor,
                                    modifier = Modifier
                                        .width(32.dp)
                                        .height(32.dp)
                                )
                                Text(
                                    text = "Notification Deleted!",
                                    color = LightAppBarIconsColor,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Nunito.Normal.font)
                                )
                            }
                        }
                    },
                    dismissContent = {
                        NotificationItemView(
                            notification = notification,
                            onItemClick ={ onNotificationClick(notification) }
                        )
                    },
                    directions = setOf(DismissDirection.EndToStart)
                )
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
            getSampleInvitationNotification()
        ),
        onNotificationClick = {},
        onDeleteNotification = {},
        onClearAll = {},
        onClickSettings = {},
        onClose = {}
    )
}

