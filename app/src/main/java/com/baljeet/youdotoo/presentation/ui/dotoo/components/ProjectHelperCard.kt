package com.baljeet.youdotoo.presentation.ui.dotoo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.views.lazies.profilesLazyRow
import com.baljeet.youdotoo.presentation.ui.theme.getOnCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun ProjectHelperCard(project: ProjectWithProfiles) {

    Box{
        Row(
            modifier = Modifier
                .shadow(elevation = 0.dp, shape = RoundedCornerShape(8.dp))
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            FilledIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = getOnCardColor()
                )
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription ="Edit Project Button",
                    tint = getTextColor()
                )
            }


            FilledIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = getOnCardColor()
                )
            ) {
                Icon(
                    Icons.Default.NotificationsOff,
                    contentDescription ="Turn off notification for this project",
                    tint = getTextColor()
                )
            }
            FilledIconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = getOnCardColor()
                )
            ) {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription ="Delete this project",
                    tint = getTextColor()
                )
            }

            project.profiles?.let { profiles ->
                profilesLazyRow(profiles = profiles, onTapProfiles = {})
            }?: kotlin.run {
                Spacer(modifier = Modifier.weight(.5f))
                FilledIconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = getOnCardColor()
                    )
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription ="Add collaborator button",
                        tint = getTextColor()
                    )
                }
            }
        }
    }
}