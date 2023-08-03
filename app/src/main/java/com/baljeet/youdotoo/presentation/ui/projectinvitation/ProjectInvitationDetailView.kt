package com.baljeet.youdotoo.presentation.ui.projectinvitation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.AccessTypeEditor
import com.baljeet.youdotoo.common.AccessTypeViewer
import com.baljeet.youdotoo.common.InvitationAccepted
import com.baljeet.youdotoo.common.InvitationDeclined
import com.baljeet.youdotoo.common.InvitationPending
import com.baljeet.youdotoo.common.getSampleInvitation
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ProjectInvitationDetailView(
    invitation: InvitationEntity?,
    acceptInvitation: (InvitationEntity) -> Unit,
    declineInvitation: (InvitationEntity) -> Unit,
    onClose: () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = if (isSystemInDarkTheme()) {
            NightDotooDarkBlue
        } else {
            NightDotooNormalBlue
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    NightDotooNormalBlue
                }
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


            /**
             * Top heading row
             * **/
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    /**
                     * Close icon button
                     * **/
                    androidx.compose.material3.IconButton(
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
                            tint = Color.White
                        )
                    }

                    /**
                     * Title
                     * **/
                    Text(
                        text = "Project Invite",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White

                    )

                    Spacer(modifier = Modifier.width(50.dp))
                }
                Spacer(modifier = Modifier.height(50.dp))
                Image(
                    painterResource(id = R.drawable.announcment_illust),
                    contentDescription = "Illustration"
                )

                val annotatedString = buildAnnotatedString {
                    val inviteeName = "@${invitation?.inviteeName?.replace(" ","")} "
                    val restText = "has invited you to this project as a "
                    val accessType = when (invitation?.accessType) {
                        AccessTypeEditor -> {
                            "Editor"
                        }

                        AccessTypeViewer -> {
                            "Viewer"
                        }

                        else -> {
                            "Admin"
                        }
                    }
                    val spanStyleBold = SpanStyle(
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        color = if (isSystemInDarkTheme()) {
                            NightDotooBrightPink
                        } else {
                            NightDotooBrightBlue
                        }
                    )
                    val spanStyleNormal = SpanStyle(
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = LessTransparentWhiteColor

                    )
                    val spanStyleUnderlined = SpanStyle(
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = LessTransparentWhiteColor,
                        textDecoration = TextDecoration.Underline
                    )
                    withStyle(style = spanStyleBold) {
                        append(inviteeName)
                    }
                    withStyle(style = spanStyleNormal) {
                        append(restText)
                    }
                    withStyle(style = spanStyleUnderlined) {
                        append(accessType)
                    }
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    fontSize = 14.sp,
                    letterSpacing = TextUnit(value = 2f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))
            }


        Column(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = if (isSystemInDarkTheme()){
                        NightDotooNormalBlue
                    }else{
                        DotooGray
                    },
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        topEnd = 30.dp
                    )
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            /**
             * Project detail Card
             * **/
            ProjectInvitationCard(
                invitation = invitation,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 30.dp,
                            bottomEnd = 30.dp,
                            bottomStart = 30.dp
                        )
                    )
            )

            Spacer(modifier = Modifier.height(30.dp))


            /**
             * Buttons
             * **/

            Spacer(modifier = Modifier.weight(1f))


            AnimatedVisibility(visible = invitation?.status == InvitationPending) {
                Button(
                    onClick = {
                        acceptInvitation(invitation!!)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            NightDotooDarkBlue
                        } else {
                            NightDotooNormalBlue
                        }
                    ),
                    shape = RoundedCornerShape(20.dp),
                    enabled = invitation != null
                ) {
                    Text(
                        text = "Accept",
                        fontFamily = FontFamily(Nunito.Bold.font),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
            AnimatedVisibility(visible = invitation?.status == InvitationPending) {
                TextButton(
                    onClick = {
                        declineInvitation(invitation!!)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = invitation != null
                ) {
                    Text(
                        text = "Decline",
                        fontFamily = FontFamily(Nunito.Bold.font),
                        color = if (isSystemInDarkTheme()) {
                            NightDotooBrightPink
                        } else {
                            NightDotooBrightBlue
                        },
                        textDecoration = TextDecoration.Underline,
                        fontSize = 16.sp
                    )
                }
            }
            AnimatedVisibility(visible = invitation?.status == InvitationAccepted ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Accepted Icon",
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        NightDotooDarkBlue
                    },
                    modifier = Modifier
                )
            }

            AnimatedVisibility(visible = invitation?.status == InvitationAccepted ) {

                Text(
                    text = "You have accepted this Invitation.",
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 20.sp,
                    color = if(isSystemInDarkTheme()){
                        Color.White
                    }else{
                        NightDotooDarkBlue
                    },
                    lineHeight = TextUnit(29f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            }


            AnimatedVisibility(visible = invitation?.status == InvitationDeclined ) {
                Icon(
                    Icons.Filled.Cancel,
                    contentDescription = "Accepted Icon",
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        NightDotooDarkBlue
                    },
                    modifier = Modifier
                )
            }

            AnimatedVisibility(visible = invitation?.status == InvitationDeclined ) {
                Text(
                    text = "You have declined this Invitation.",
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 20.sp,
                    color = if(isSystemInDarkTheme()){
                        Color.White
                    }else{
                        NightDotooDarkBlue
                    },
                    lineHeight = TextUnit(29f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectInvitationDetailView() {
    ProjectInvitationDetailView(
        invitation = getSampleInvitation(),
        acceptInvitation = {},
        declineInvitation = {},
        onClose = {}
    )
}