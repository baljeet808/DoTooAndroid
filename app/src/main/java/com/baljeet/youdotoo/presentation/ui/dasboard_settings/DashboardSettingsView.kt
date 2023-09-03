package com.baljeet.youdotoo.presentation.ui.dasboard_settings

import android.content.res.Configuration
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import com.baljeet.youdotoo.presentation.ui.dasboard_settings.components.DashboardTasksListTypePreview
import com.baljeet.youdotoo.presentation.ui.dasboard_settings.components.ProjectViewPreview
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor

@Composable
fun DashboardSettingsView(
    goBack: () -> Unit
) {

    SharedPref.init(LocalContext.current)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    getDarkThemeColor()
                } else {
                    getNightLightColor()
                }
            )
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
                onClick = goBack,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = getLightThemeColor(),
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
                text = "Dashboard Settings",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(50.dp))
        }


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {

            item {
               ProjectViewPreview()
            }

            item {
                DashboardTasksListTypePreview()
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashboardSettingsView() {
    DashboardSettingsView(
        goBack = {}
    )
}