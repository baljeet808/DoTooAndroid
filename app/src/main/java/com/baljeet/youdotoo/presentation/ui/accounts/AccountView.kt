package com.baljeet.youdotoo.presentation.ui.accounts

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CancelPresentation
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.buildMoneyAnnotatedString
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun AccountView(
    user : UserEntity?,
    onClose : () -> Unit
) {
    SharedPref.init(LocalContext.current)

    val screenWidthInDp = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            ),
        verticalArrangement = Arrangement.Top
    ) {

        /**
         * Top row
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * Close icon button
             * **/
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = getDarkThemeColor(),
                        shape = RoundedCornerShape(40.dp)
                    )

            ) {
                Icon(
                    Icons.Default.ArrowBackIos,
                    contentDescription = "Button to close current screen.",
                    tint = getTextColor()
                )
            }

            /**
             * Title
             * **/
            Text(
                text = "Account",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = getTextColor()
            )
            Spacer(modifier = Modifier.width(50.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = getDarkThemeColor()
                )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            item {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "About Me",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.ExtraBold,
                    color = getTextColor()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {


                        Text(
                            text = user?.name?:"",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontWeight = FontWeight.ExtraBold,
                            color = getTextColor()
                        )

                        Text(
                            text = user?.email?:"",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontWeight = FontWeight.ExtraBold,
                            color = getTextColor()
                        )

                        Text(
                            text = "Joined ${LocalContext.current.getString(R.string.app_name)} ".plus(user?.joined?.toNiceDateTimeFormat()),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Gray
                        )
                    }

                    AsyncImage(
                        model = user?.avatarUrl?:"",
                        contentDescription = "avatarImage",
                        placeholder = painterResource(id = R.drawable.feeling_good),
                        modifier = Modifier
                            .width((screenWidthInDp / 5).dp)
                            .height((screenWidthInDp / 5).dp)
                            .clip(shape = RoundedCornerShape(200.dp)),
                        contentScale = ContentScale.Crop

                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = getDarkThemeColor()
                        )
                )

            }
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Selected Subscription",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.ExtraBold,
                    color = getTextColor()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    /**
                     * item name
                     * **/
                    androidx.compose.material.Text(
                        text = "No subscription",
                        color = getTextColor(),
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 16.sp,
                        modifier = Modifier,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Free",
                        modifier = Modifier,
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        color = DotooBlue
                    )

                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = SharedPref.isUserAPro.not()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                            .background(
                                color = getDarkThemeColor(),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                Icons.Default.Sell,
                                contentDescription = "Buy Pro membership",
                                tint = getTextColor(),
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Be A Pro Member",
                                color = getTextColor(),
                                fontFamily = FontFamily(Nunito.Normal.font),
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Text(
                            text = buildMoneyAnnotatedString(getTextColor(), "4.99", 34.sp),
                            fontFamily = FontFamily(Nunito.Normal.font),
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "More details >>",
                            fontFamily = FontFamily(Nunito.Normal.font),
                            color = getTextColor(),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            textAlign = TextAlign.End
                        )
                    }
                }

                AnimatedVisibility(visible= SharedPref.isUserAPro) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp)
                            .background(
                                color = getDarkThemeColor(),
                                shape = RoundedCornerShape(10.dp)
                            )

                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(15.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                Icons.Default.CancelPresentation,
                                contentDescription = "Cancel button",
                                tint = getTextColor(),
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Cancel Membership",
                                color = getTextColor(),
                                fontFamily = FontFamily(Nunito.Normal.font),
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                    }
                }


                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = getDarkThemeColor()
                        )
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Actions",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.ExtraBold,
                    color = getTextColor()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .background(
                            color = getDarkThemeColor(),
                            shape = RoundedCornerShape(10.dp)
                        )

                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "logout button",
                            tint = getTextColor(),
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Log out",
                            color = getTextColor(),
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                        .background(
                            color = getDarkThemeColor(),
                            shape = RoundedCornerShape(10.dp)
                        )

                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(15.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            Icons.Default.DeleteForever,
                            contentDescription = "Delete account button",
                            tint = getTextColor(),
                            modifier = Modifier
                                .height(30.dp)
                                .width(30.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Delete Account",
                            color = getTextColor(),
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                    }

                }
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAccountView(){
    AccountView(
        user = getSampleProfile().toUserEntity(),
        onClose = {}
    )
}