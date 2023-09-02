package com.baljeet.youdotoo.presentation.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.settings.components.AccountSectionView
import com.baljeet.youdotoo.presentation.ui.settings.components.PersonalisationSectionView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun SettingsView(
    onClose: () -> Unit,
    onClickAccount : () -> Unit,
    onClickThemes: () -> Unit,
    onClickDashboard: () -> Unit,
    onClickDialogPref : () -> Unit
) {

    SharedPref.init(LocalContext.current)
    SharedPref.deleteTaskWithoutConfirmation = false
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(color = getLightThemeColor())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            )
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {


        /**
         * Top row
         * **/
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 20.dp, end = 20.dp),
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
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            /**
             * Title
             * **/
            Text(
                text = "Settings",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start,
                fontSize = 30.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = getTextColor()
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {


            item {
                Spacer(modifier = Modifier.height(30.dp))
            }

            /**
             * section for account settings
             * **/
            item {
                AccountSectionView (
                    onClickAccount = onClickAccount
                )
            }

            item {
                Spacer(modifier = Modifier.height(25.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(
                            color = getDarkThemeColor()
                        )
                )
                Spacer(modifier = Modifier.height(25.dp))
            }

            /**
             * section personalisation
             * **/
            item {
                PersonalisationSectionView (
                    onClickThemes = onClickThemes,
                    onClickDashboard = onClickDashboard,
                    onClickDialogPref = onClickDialogPref
                )
            }

        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSettingsView() {
    SettingsView(
        onClose = {},
        onClickThemes = {},
        onClickDashboard = {},
        onClickDialogPref = {},
        onClickAccount= {}
    )
}