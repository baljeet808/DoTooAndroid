package com.baljeet.youdotoo.presentation.ui.invitation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Rsvp
import androidx.compose.material3.Icon
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
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink

@Composable
fun InvitePeopleHeading(
    showSubHeading : Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /**
         * RSVP icon
         * **/
        Icon(
            Icons.Outlined.Rsvp,
            contentDescription = "RSVP icon",
            tint = if (isSystemInDarkTheme()) {
                NightDotooBrightPink
            } else {
                NightDotooBrightBlue
            },
            modifier = Modifier
                .width(70.dp)
                .height(50.dp)
        )

        /**
         * First heading
         * **/
        Text(
            text = "Invite People to this Project",
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 22.sp,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))

        /**
         * Helper text
         * **/
        AnimatedVisibility(visible = showSubHeading) {
            Text(
                text = "Quickly invite from members, with whom you collaborated earlier",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 15.sp,
                color = if (isSystemInDarkTheme()) {
                    LightDotooFooterTextColor
                } else {
                    Color.Gray
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewInvitePeopleHeading() {
    InvitePeopleHeading(
        showSubHeading = true
    )
}

