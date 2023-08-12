package com.baljeet.youdotoo.presentation.ui.profile_quick_view

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightPink
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun ProfileQuickView(
    user: UserEntity?,
    goBack: () -> Unit
) {

    SharedPref.init(LocalContext.current)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val screenWidthInDp = LocalConfiguration.current.screenWidthDp
        val screenHeightInDp = LocalConfiguration.current.screenHeightDp

        Canvas(modifier = Modifier.fillMaxWidth(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = (screenWidthInDp / 1.8).dp.toPx(),
                center = Offset(
                    x = (screenWidthInDp / 2).dp.toPx(),
                    y = (screenHeightInDp / 6).dp.toPx()
                )
            )
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 500.dp.toPx(),
                center = Offset(
                    x = (screenWidthInDp / 2).dp.toPx(),
                    y = (screenHeightInDp / 5).dp.toPx()
                )
            )
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = (screenWidthInDp / 3.5).dp.toPx(),
                center = Offset(
                    x = (screenWidthInDp / 2).dp.toPx(),
                    y = (screenWidthInDp / 4 + 20).dp.toPx()
                )
            )
        })


        Spacer(modifier = Modifier.height(20.dp))

        user?.let {
            AsyncImage(
                model = user.avatarUrl,
                contentDescription = "avatarImage",
                placeholder = painterResource(id = R.drawable.feeling_good),
                modifier = Modifier
                    .width((screenWidthInDp / 2).dp)
                    .height((screenWidthInDp / 2).dp)
                    .clip(shape = RoundedCornerShape(300.dp)),
                contentScale = ContentScale.Crop

            )


            val annotatedIntro = buildAnnotatedString {
                val mainText = "Hi there 👋. I am "

                val nameStyle = SpanStyle(
                    color = Color(EnumProjectColors.Peach.longValue)
                )

                val emailStyle = SpanStyle(
                    color = NightDotooBrightBlue
                )

                val joinedDateStyle = SpanStyle(
                    color = Color.Gray
                )

                val appNameStyle = SpanStyle(
                    color = NightDotooBrightPink
                )

                append(mainText)

                withStyle(style = nameStyle) {
                    append(user.name)
                }

                append(" and my email address is ")

                withStyle(style = emailStyle) {
                    append(user.email)
                }

                append(". I am using ")

                withStyle(style = appNameStyle) {
                    append(stringResource(id = R.string.app_name))
                }

                append(" from ")

                withStyle(style = joinedDateStyle) {
                    append(user.joined.toNiceDateTimeFormat())
                }

                append(".")
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = user.name,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = getTextColor()
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = annotatedIntro,
                fontFamily = FontFamily(Nunito.Normal.font),
                color = getTextColor(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit(1f, type = TextUnitType.Sp),
                lineHeight = TextUnit(25f, type = TextUnitType.Sp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick =  goBack ,
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
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Button to close side drawer.",
                tint = getTextColor()
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProfileQuickView() {
    ProfileQuickView(
        user = getSampleProfile().toUserEntity(),
        goBack = {}
    )
}