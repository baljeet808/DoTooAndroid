package com.baljeet.youdotoo.presentation.ui.shared.views.bottomSheets

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor


@Composable
fun SelectAccessTypeSheet(
    access: Int,
    onAccessChanged: (Int) -> Unit,
    email: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = getDarkThemeColor(),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Assign permissions".plus(
                    email?.let {
                        " to $it"
                    } ?: ""
                ),
                fontFamily = FontFamily(Nunito.SemiBold.font),
                fontSize = 24.sp,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                },
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(20.dp))
            for (accessType in 1..2) {
                Spacer(modifier = Modifier.height(10.dp))


                Column(
                    modifier = Modifier
                        .border(
                            width = if (accessType == access) {
                                3.dp
                            } else 1.dp,
                            color = if (isSystemInDarkTheme()) {
                                NightDotooFooterTextColor
                            } else {
                                LightDotooFooterTextColor
                            },
                            shape = RoundedCornerShape(30.dp)
                        )
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (accessType == 1) {
                                "Editor"
                            } else "Viewer",
                            color = if (isSystemInDarkTheme()) {
                                Color.White
                            } else {
                                Color.Black
                            },
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f),
                        )

                        Checkbox(
                            checked = accessType == access,
                            onCheckedChange = {
                                onAccessChanged(accessType)
                            },
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = Color.White,
                                checkedColor = NightDotooBrightBlue,
                                uncheckedColor = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )
                        )
                    }

                    Text(
                        text = if (accessType == 1) {
                            "An Editor can create, edit, check, uncheck and delete tasks. Editor can not modify project settings. Editor can send messages in task related chat."
                        } else {
                             "A Viewer can only see project and tasks. Viewer can also send messages in task related chat. "
                        },
                        color = if (isSystemInDarkTheme()) {
                            LightDotooFooterTextColor
                        } else {
                            Color.Gray
                        },
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth(),
                    )

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSelectAccessTypeSheet() {
    SelectAccessTypeSheet(access = 1, onAccessChanged = {})
}