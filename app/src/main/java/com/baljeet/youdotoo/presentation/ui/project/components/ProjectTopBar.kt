package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun ProjectTopBar(
    notificationsState : Boolean,
    onNotificationItemClicked: () -> Unit,
    onDeleteItemClicked: () -> Unit,
    onClickInvite: () -> Unit,
    role : EnumRoles,
    modifier: Modifier
) {


    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * Role
             * **/
            Text(
                text = "You are ".plus(role.name),
                modifier = Modifier
                    .padding(start = 10.dp, end = 5.dp),
                color = LessTransparentWhiteColor,
                fontSize = 14.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
            )


            Spacer(modifier = Modifier.weight(.2f))

            /**
             * Button to add more person to the project
             * **/
            if(role == EnumRoles.Admin || role == EnumRoles.ProAdmin) {
                IconButton(
                    onClick = onClickInvite,
                    modifier = Modifier
                        .weight(0.2f)
                ) {
                    Icon(
                        Icons.Outlined.PersonAdd,
                        contentDescription = "Button to add more person to the project",
                        tint = Color.White
                    )
                }
                /**
                 * Delete the project
                 * **/
                IconButton(
                    onClick = {
                        onDeleteItemClicked()
                    },
                    modifier = Modifier
                        .weight(0.2f)
                ) {
                    Icon(
                        Icons.Outlined.DeleteForever,
                        contentDescription = "Button to Delete the project",
                        tint = Color.White
                    )
                }
            }

            /**
             * Silent Notification for this project
             * **/
            IconButton(
                onClick = {
                    onNotificationItemClicked()
                },
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Icon(

                    if (notificationsState){
                        Icons.Rounded.VolumeUp
                    }else{
                        Icons.Rounded.VolumeMute
                    },
                    contentDescription = "Button to Delete the project",
                    tint = Color.White
                )
            }

        }

    }


}


@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewProjectTopBar() {
    ProjectTopBar(
       modifier = Modifier,
        notificationsState = true,
        onNotificationItemClicked = {},
        onDeleteItemClicked = {},
        onClickInvite = {},
        role = EnumRoles.Admin
    )
}
