package com.baljeet.youdotoo.presentation.ui.theme

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.presentation.ui.dotoo.DummyDoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.projects.components.DummyProjectCardView
import com.baljeet.youdotoo.presentation.ui.projects.getUserRole
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito


@Composable
fun ThemeChooserView(
    onClose: () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightNormalThemeColor
                } else {
                    DotooGray
                }
            ),
        verticalArrangement = Arrangement.Top
    ) {


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
            androidx.compose.material3.IconButton(
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

            /**
             * Title
             * **/
            Text(
                text = "Customise theme",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )
            Spacer(modifier = Modifier.width(50.dp))
        }



        Text(
            text = "Preview".uppercase(),
            color = LightAppBarIconsColor,
            fontFamily = FontFamily(Nunito.Normal.font),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start,
            letterSpacing = TextUnit(2f, TextUnitType.Sp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .background(
                        color = NightNormalThemeColor,
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 20.dp
                        )
                    )
            ) {
                Text(
                    text = "Projects".uppercase(),
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Start,
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )


                DummyProjectCardView(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 0.dp,
                        bottom = 10.dp
                    ),
                    project = getSampleProjectWithTasks(),
                    onItemClick = {},
                    role = getSampleProject().getUserRole(userId = getRandomId()),
                    backgroundColor = NightDarkThemeColor,
                    leftAlign = false,
                    textColor = Color.White
                )

                Text(
                    text = "TODAY".uppercase(),
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Start,
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )

                DummyDoTooItemsLazyColumn(
                    doToos = listOf(
                        getSampleDotooItem(),
                        getSampleDotooItem()
                    ),
                    modifier = Modifier.padding(start = 10.dp, bottom = 20.dp),
                    textColor = Color.White,
                    backgroundColor = NightDarkThemeColor
                )

            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .shadow(
                        elevation = 5.dp,
                        shape = RoundedCornerShape(
                            topEnd = 0.dp,
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 0.dp
                        )
                    )
                    .background(
                        color = DotooGray,
                        shape = RoundedCornerShape(
                            topEnd = 0.dp,
                            topStart = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 0.dp
                        )
                    )
            ) {

                Text(
                    text = "Projects".uppercase(),
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Start,
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )


                DummyProjectCardView(
                    modifier = Modifier.padding(
                        top = 10.dp,
                        start = 10.dp,
                        end = 0.dp,
                        bottom = 10.dp
                    ),
                    project = getSampleProjectWithTasks(),
                    onItemClick = {},
                    role = getSampleProject().getUserRole(userId = getRandomId()),
                    backgroundColor = Color.White,
                    leftAlign = false,
                    textColor = Color.Black
                )


                Text(
                    text = "TODAY".uppercase(),
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Start,
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )

                DummyDoTooItemsLazyColumn(
                    doToos = listOf(
                        getSampleDotooItem(),
                        getSampleDotooItem()
                    ),
                    modifier = Modifier.padding(start = 10.dp, bottom = 20.dp),
                    textColor = Color.Black,
                    backgroundColor = Color.White
                )

            }

        }


        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Dark",
                color = LightAppBarIconsColor,
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 13.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit(2f, TextUnitType.Sp)
            )
            Text(
                text = "Light",
                color = LightAppBarIconsColor,
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 13.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit(2f, TextUnitType.Sp)
            )
        }


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Select color".uppercase(),
            color = LightAppBarIconsColor,
            fontFamily = FontFamily(Nunito.Normal.font),
            fontSize = 14.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            textAlign = TextAlign.Start,
            letterSpacing = TextUnit(2f, TextUnitType.Sp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewThemeChooserView() {
    ThemeChooserView(
        onClose = {}
    )
}