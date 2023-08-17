package com.baljeet.youdotoo.presentation.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getDayLightColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun PersonalisationSectionView(
    onClickThemes: () -> Unit
) {
    SharedPref.init(LocalContext.current)
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        /**
         * account heading
         * **/
        Text(
            text = "Personalisation",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontFamily = FontFamily(Nunito.Bold.font),
            fontWeight = FontWeight.ExtraBold,
            color = getTextColor()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClickThemes),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * User Profile with Progress bar for total tasks completed
             * **/
            Icon(
                Icons.Default.ColorLens,
                contentDescription = "Themes icons",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .clip(shape = RoundedCornerShape(65.dp)),
                tint = getDarkThemeColor()
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
                    text = "Change theme",
                    color = getTextColor(),
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
                    text = "theme previews and dark mode",
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
fun PreviewPersonalisationSectionView(){
    PersonalisationSectionView(
        onClickThemes = {}
    )
}