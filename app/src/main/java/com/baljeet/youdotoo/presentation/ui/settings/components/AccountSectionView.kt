package com.baljeet.youdotoo.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDayLightColor

@Composable
fun AccountSectionView(
    onClickAccount : () -> Unit
) {
    SharedPref.init(LocalContext.current)
    Column(
        modifier = Modifier
            .fillMaxWidth().padding(top = 20.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        /**
         * account heading
         * **/
        Text(
            text = "Account",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontFamily = FontFamily(Nunito.Bold.font),
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClickAccount
                )
            ,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * User Profile with Progress bar for total tasks completed
             * **/
            AsyncImage(
                model = SharedPref.userAvatar,
                contentDescription = "avatarImage",
                placeholder = painterResource(id = R.drawable.youdotoo_app_icon),
                modifier = Modifier
                    .width(65.dp)
                    .height(65.dp)
                    .clip(shape = RoundedCornerShape(65.dp))
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column (
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {

                /**
                 * User name
                 * **/
                Text(
                    text = SharedPref.userEmail,
                    color = Color.White,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                /**
                 * settings info
                 * **/
                Text(
                    text = "Subscription and profile",
                    color = Color.Gray,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription ="Navigate button",
                tint = getDayLightColor()
            )

        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAccountSectionView(){
    AccountSectionView(
        onClickAccount = {}
    )
}