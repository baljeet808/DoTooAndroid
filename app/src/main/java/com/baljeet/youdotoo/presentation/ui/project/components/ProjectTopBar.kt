package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.rounded.VolumeMute
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun ProjectTopBar(
    notificationsState : Boolean,
    onMenuItemClick: () -> Unit,
    onNotificationItemClicked: () -> Unit,
    onDeleteItemClicked: () -> Unit,
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
             * Back button
             * **/
            IconButton(
                onClick = {
                    onMenuItemClick()
                },
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = "Back button for navigating back to dashboard",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(.5f))

            /**
             * Button to add more person to the project
             * **/
            IconButton(
                onClick = {

                },
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
                    onNotificationItemClicked()
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


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectTopBar() {
    ProjectTopBar(
       modifier = Modifier,
        notificationsState = true,
        onMenuItemClick = {},
        onNotificationItemClicked = {},
        onDeleteItemClicked = {}
    )
}
