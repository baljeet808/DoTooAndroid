package com.baljeet.youdotoo.presentation.ui.drawer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Person2
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Topic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.ConstSampleAvatarUrl
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun NavigationDrawer(
    userData: UserData
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    NightDotooNormalBlue
                }
            )
            .padding(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(0.dp), contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = userData.profilePictureUrl,
                        contentDescription = "avatarImage",
                        placeholder = painterResource(id = com.baljeet.youdotoo.R.drawable.feeling_good),
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .clip(shape = RoundedCornerShape(80.dp))
                    )

                    CircularProgressIndicator(
                        modifier = Modifier
                            .progressSemantics()
                            .size(100.dp),
                        progress = .40F,
                        trackColor = if (isSystemInDarkTheme()) {
                            NightDotooLightBlue
                        } else {
                            NightDotooFooterTextColor
                        },
                        color = NightDotooBrightPink
                    )
                }


                IconButton(
                    onClick = { /*TODO*/ },
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
                        contentDescription = "Close drawer",
                        tint = if (isSystemInDarkTheme()) {
                            NightDotooTextColor
                        } else {
                            LightDotooFooterTextColor
                        }
                    )
                }

            }


            val userName = userData.userName
                ?.split(" ", limit = 3)
                ?.toCollection(ArrayList())
                ?: arrayListOf("Unknown", "User")
            var userNameMultiline = ""

            for (word in userName) {
                userNameMultiline += word
                if (word != userName.last()) {
                    userNameMultiline += "\n"
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = userNameMultiline,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                fontSize = 36.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) {
                    NightDotooTextColor
                } else {
                    LightDotooFooterTextColor
                }
            )

            Spacer(modifier = Modifier.height(30.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Outlined.Person2,
                    contentDescription = "profile button",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooFooterTextColor
                    } else {
                        LightDotooFooterTextColor
                    }
                )
                
                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Account",
                    fontFamily = FontFamily(Nunito.Normal.font),
                    color = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        LightDotooFooterTextColor
                    },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Outlined.Topic,
                    contentDescription = "Projects button",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooFooterTextColor
                    } else {
                        LightDotooFooterTextColor
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Projects",
                    fontFamily = FontFamily(Nunito.Normal.font),
                    color = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        LightDotooFooterTextColor
                    },

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Outlined.Settings,
                    contentDescription = "Setting button",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooFooterTextColor
                    } else {
                        LightDotooFooterTextColor
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Settings",
                    fontFamily = FontFamily(Nunito.Normal.font),
                    color = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        LightDotooFooterTextColor
                    },

                )
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewNavigationDrawer() {
    NavigationDrawer(
        userData = UserData(
            userId = "",
            userName = "Karandeep Kaur",
            userEmail = "Karanwaraich45@gmail.com",
            profilePictureUrl = ConstSampleAvatarUrl
        )
    )
}