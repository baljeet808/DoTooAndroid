package com.baljeet.youdotoo.presentation.ui.drawer

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.ConstSampleAvatarUrl
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.menuItems
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.domain.models.MenuItem
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooLightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightDarkColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor
import java.lang.Float.max

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun NavigationDrawer(
    userData: UserData,
    menuItems: List<MenuItem>,
    onMenuItemClick: (MenuItem) -> Unit,
    closeDrawer : () -> Unit,
    logout: () -> Unit,
    modifier: Modifier,
    allTasks : List<DoTooItemEntity>
) {


    val animatedProgress = animateFloatAsState(
        targetValue = (max(0.1f,allTasks.filter { task -> task.done }.size.toFloat()) / max(1F,(allTasks.size).toFloat())),
        animationSpec = tween(
            delayMillis = 1500,
            durationMillis = 1500,
            easing = LinearEasing
        ), label = ""
    ).value

    val transition = rememberInfiniteTransition(label = "")

    val offsetX by transition.animateValue(
        initialValue = (-40).dp,
        targetValue = 20.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )
    val offsetY by transition.animateValue(
        initialValue = (300).dp,
        targetValue = 450.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )

    val offsetX1 by transition.animateValue(
        initialValue = (-60).dp,
        targetValue = 20.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )
    val offsetY1 by transition.animateValue(
        initialValue = 450.dp,
        targetValue = 300.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter, label = ""
    )



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    getNightDarkColor()
                } else {
                    getNightLightColor()
                }
            )
            .padding(20.dp)
    ) {


        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 230.dp.toPx(),
                center = Offset(
                    x = offsetX1.toPx(),
                    y = offsetY1.toPx()
                )
            )
        })

        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 180.dp.toPx(),
                center = Offset(
                    x = offsetX.toPx(),
                    y = offsetY.toPx()
                )
            )
        })


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {

            /**
             * Top row to show profile image and drawer close button
             * **/
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                /**
                 * User Profile with Progress bar for total tasks completed
                 * **/
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
                        progress = animatedProgress,
                        trackColor = if (isSystemInDarkTheme()) {
                            NightDotooLightBlue
                        } else {
                            NightDotooFooterTextColor
                        },
                        color = NightDotooBrightPink
                    )
                }


                /**
                 * Drawer close icon button
                 * **/
                IconButton(
                    onClick = { closeDrawer() },
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
                        contentDescription = "Button to close side drawer.",
                        tint = if (isSystemInDarkTheme()) {
                            NightDotooTextColor
                        } else {
                            LightDotooFooterTextColor
                        }
                    )
                }

            }


            /**
             * Breaking user name into separate lines
             * **/
            var userNameMultiline = ""
            val userName = userData.userName
                ?.split(" ", limit = 3)
                ?.toCollection(ArrayList())
                ?: arrayListOf("Unknown", "User")
            for (word in userName) {
                userNameMultiline += word
                if (word != userName.last()) {
                    userNameMultiline += "\n"
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


            /**
             * Showing user name
             * **/
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


            userData.userEmail?.let { email ->
                /**
                 * Showing user email
                 * **/
                Text(
                    text = email,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.ExtraBold,
                    color = LessTransparentWhiteColor
                )
                Spacer(modifier = Modifier.height(30.dp))
            }






            /**
             * Lazy column for Menu items
             * **/
            LazyColumn(modifier = Modifier.fillMaxWidth()) {

                items(menuItems) { menuItem ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable(
                                onClick = {
                                    onMenuItemClick(menuItem)
                                }
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            menuItem.icon,
                            contentDescription = menuItem.contentDescription,
                            tint = if (isSystemInDarkTheme()) {
                                NightDotooFooterTextColor
                            } else {
                                LightDotooFooterTextColor
                            }
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = menuItem.title,
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


            Spacer(modifier = Modifier.weight(1f))

            /**
             *Logout button
             * **/
            Row(
                modifier = Modifier
                    .widthIn(max = 280.dp, min = 150.dp)
                    .background(
                        color = NightTransparentWhiteColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 10.dp)
                    .clickable(
                        onClick = logout
                    )
                ,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ){

                Text(
                    text = "Logout",
                    color = Color.White,
                    fontFamily = FontFamily(Nunito.Bold.font),

                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    Icons.Default.Logout,
                    contentDescription = "LogoUt Button",
                    tint = Color.White
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
        ),
        menuItems = menuItems,
        onMenuItemClick = {},
        closeDrawer = {},
        logout = {},
        modifier = Modifier,
        allTasks = listOf(
            getSampleDotooItem().toDoTooItemEntity("0")
        )
    )
}