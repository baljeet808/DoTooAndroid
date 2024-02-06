package com.baljeet.youdotoo.presentation.ui.dialogs_settings

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
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getColor
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightLightColor

@Composable
fun AppPreferencesView(
    goBack: () -> Unit
) {

    SharedPref.init(LocalContext.current)

    var deleteTaskWithoutConfirmation by remember {
        mutableStateOf(SharedPref.deleteTaskWithoutConfirmation)
    }
    var doNotBugMeAboutProFeaturesAgain by remember {
        mutableStateOf(SharedPref.doNotBugMeAboutProFeaturesAgain)
    }
    var doNotShowViewerTasksOnDashboard by remember {
        mutableStateOf(SharedPref.doNotShowViewerTasksOnDashboard)
    }


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
                    Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Button to close current screen.",
                    tint = Color.White
                )
            }

            /**
             * Title
             * **/
            Text(
                text = "App Preferences",
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
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = getLightThemeColor(),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Delete tasks without confirmation",
                            color = Color.White,
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Checkbox(
                            checked = deleteTaskWithoutConfirmation,
                            onCheckedChange = {
                                deleteTaskWithoutConfirmation = it
                                SharedPref.deleteTaskWithoutConfirmation = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = EnumProjectColors.Indigo.name.getColor(),
                                uncheckedColor = EnumProjectColors.Indigo.name.getColor()
                            ),
                            modifier = Modifier.width(25.dp)
                        )
                    }

                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = getLightThemeColor(),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Do not show pro features pop up",
                            color = Color.White,
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Checkbox(
                            checked = doNotBugMeAboutProFeaturesAgain,
                            onCheckedChange = {
                                doNotBugMeAboutProFeaturesAgain = it
                                SharedPref.doNotBugMeAboutProFeaturesAgain = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = EnumProjectColors.Indigo.name.getColor(),
                                uncheckedColor = EnumProjectColors.Indigo.name.getColor()
                            ),
                            modifier = Modifier.width(25.dp)
                        )
                    }

                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = getLightThemeColor(),
                                shape = RoundedCornerShape(0.dp)
                            )
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hide tasks from dashboard which have viewer access",
                            color = Color.White,
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Checkbox(
                            checked = doNotShowViewerTasksOnDashboard,
                            onCheckedChange = {
                                doNotShowViewerTasksOnDashboard = it
                                SharedPref.doNotShowViewerTasksOnDashboard = it
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = EnumProjectColors.Indigo.name.getColor(),
                                uncheckedColor = EnumProjectColors.Indigo.name.getColor()
                            ),
                            modifier = Modifier.width(25.dp)
                        )
                    }

                }
            }



            item {
                Spacer(modifier = Modifier.height(40.dp))
            }

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppPreferencesView() {
    AppPreferencesView(
        goBack = {}
    )
}