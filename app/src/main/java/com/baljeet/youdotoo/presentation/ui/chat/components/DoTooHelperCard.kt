package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.views.lazies.profilesLazyRow
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getOnCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun DoTooHelperCard(doToo: DoTooWithProfiles) {

    Box{
        Row(
            modifier = Modifier
                .shadow(elevation = 0.dp, shape = RoundedCornerShape(8.dp))
                .background(
                    color = getCardColor(),
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
                    contentDescription ="Turn off notification for this doToo",
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
                    contentDescription ="Delete this doToo",
                    tint = getTextColor()
                )
            }

            doToo.profiles?.let { profiles ->
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