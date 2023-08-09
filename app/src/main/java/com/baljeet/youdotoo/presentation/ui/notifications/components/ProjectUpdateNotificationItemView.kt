package com.baljeet.youdotoo.presentation.ui.notifications.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleMessageNotification
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.data.local.entities.NotificationEntity
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor

@Composable
fun ProjectUpdateNotificationItemView(
    notification: NotificationEntity,
    onItemClick : () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .background(
                color = getDarkThemeColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        Alignment.Start
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            androidx.compose.material.Icon(
                Icons.Outlined.Message,
                contentDescription = "Menu button to open notification settings",
                tint = if (isSystemInDarkTheme()) {
                    NightDotooTextColor
                } else {
                    LightDotooFooterTextColor
                },
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
                    .border(
                        width = 1.dp,
                        color = Color(notification.projectColor),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(4.dp)

            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = notification.title,
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 16.sp,
                color = if(isSystemInDarkTheme()){
                    Color.White
                }else{
                    Color.Black
                },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        Text(
            text = notification.contentText,
            fontFamily = FontFamily(Nunito.Normal.font),
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = notification.createdAt.toNiceDateTimeFormat(true),
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 11.sp,
            color = Color(notification.projectColor),
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectUpdateNotificationItemView() {
    ProjectUpdateNotificationItemView(
        notification = getSampleMessageNotification(),
        onItemClick = {}
    )
}