package com.baljeet.youdotoo.presentation.ui.invitation.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.UserInvitation
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*


@Composable
fun InvitationItemView(
    userInvitation: UserInvitation,
    onClickButton: () -> Unit,
    onEditAccess : () -> Unit
) {

    //null if user is not invited yet
    val userInvitationStatus: Int? = userInvitation.invitationEntity?.status


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        /**
         * Profile avatar
         * placeholder is set to app icon in case user has not accepted the invitation
         * **/
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(0.dp)
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDotooDarkBlue
                    } else {
                        Color.White
                    },
                    shape = RoundedCornerShape(30.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            userInvitation.user?.let { user ->
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "avatarImage",
                    placeholder = painterResource(id = R.drawable.youdotoo_app_icon),
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
                        .clip(shape = RoundedCornerShape(80.dp))
                )
            } ?: kotlin.run {
                Image(
                    painterResource(id = R.drawable.youdotoo_app_icon),
                    contentDescription = "Placeholder image",
                    modifier = Modifier
                        .width(56.dp)
                        .height(56.dp)
                        .clip(shape = RoundedCornerShape(80.dp))
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))
        /**
         * User name, email and type
         *
         * **/
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround
        ) {


            Text(
                text = userInvitation.user?.email ?: userInvitation.invitationEntity?.invitedEmail
                ?: "",
                fontSize = 15.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            AnimatedVisibility(visible = userInvitation.user?.name != null) {
                Text(
                    text = userInvitation.user?.name ?: "",
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    color = if (isSystemInDarkTheme()) {
                        LightDotooFooterTextColor
                    } else {
                        Color.Gray
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }

            AnimatedVisibility(visible = userInvitation.invitationEntity != null) {
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = onEditAccess
                        )
                ) {
                    Text(
                        text = when (userInvitation.invitationEntity!!.accessType) {
                            0 -> "Admin"
                            1 -> "Editor"
                            2 -> "Visitor"
                            else -> "Visitor"
                        },
                        fontSize = 12.sp,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        color = if (isSystemInDarkTheme()) {
                            LightDotooFooterTextColor
                        } else {
                            Color.Gray
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier,
                        textAlign = TextAlign.Start,
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Edit access button"
                    )
                }

            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        /**
         * Add and remove button along with access modifier
         * **/
        Column(
            modifier = Modifier.padding(top = 15.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = when (userInvitationStatus) {
                    InvitationPending -> {
                        "Cancel"
                    }
                    InvitationAccepted -> {
                        "Remove"
                    }
                    InvitationDeclined -> {
                        "Resend"
                    }
                    else -> {
                        "  Add  "
                    }
                },
                fontFamily = FontFamily(Nunito.Bold.font),
                color = if (userInvitationStatus in 0..2) {
                    DoTooRed
                } else {
                    if (isSystemInDarkTheme()) {
                        LightDotooFooterTextColor
                    } else {
                        Color.Gray
                    }
                },
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            NightDotooDarkBlue
                        } else {
                            Color.White
                        },
                        shape = RoundedCornerShape(5.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooDarkBlue
                        } else {
                            DotooGray
                        },
                        shape = RoundedCornerShape(5.dp)
                    )
                    .clickable(
                        onClick = onClickButton
                    )
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp),
                textAlign = TextAlign.Start
            )
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewInvitationItemView() {
    InvitationItemView(
        userInvitation = UserInvitation(
            user = getSampleProfile().toUserEntity(),
            invitationEntity = getSampleInvitation()
        ),
        onEditAccess = {},
        onClickButton = {}
    )
}