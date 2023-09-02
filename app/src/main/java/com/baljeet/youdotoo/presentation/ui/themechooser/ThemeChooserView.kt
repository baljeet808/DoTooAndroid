package com.baljeet.youdotoo.presentation.ui.themechooser

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleColorPalette
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.data.local.entities.ColorPaletteEntity
import com.baljeet.youdotoo.presentation.ui.dotoo.DummyDoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.projects.components.DummyProjectCardView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.baljeet.youdotoo.presentation.ui.themechooser.components.ColorPalettes


@Composable
fun ThemeChooserView(
    onClose: () -> Unit,
    palettes: List<ColorPaletteEntity>,
    onSelectColorPalette: (newPalette: ColorPaletteEntity) -> Unit
) {

    SharedPref.init(LocalContext.current)

    var appliedPalette by remember {
        mutableStateOf(
            ColorPaletteEntity(
                id = SharedPref.themePaletteId,
                paletteName = SharedPref.selectedColorPalette,
                nightDark = SharedPref.themeNightDarkColor,
                nightLight = SharedPref.themeNightLightColor,
                dayDark = SharedPref.themeDayDarkColor,
                dayLight = SharedPref.themeDayLightColor,
            )
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Customise theme",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontWeight = FontWeight.ExtraBold,
                color = getTextColor()
            )
            Spacer(modifier = Modifier.width(50.dp))
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = NightTransparentWhiteColor
                )
        ) {

            Text(
                text = "Preview".uppercase(),
                color = getTextColor(),
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                letterSpacing = TextUnit(2f, TextUnitType.Sp)
            )

            Spacer(modifier = Modifier.height(20.dp))

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
                            color = Color(appliedPalette.nightLight),
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
                        role = EnumRoles.Editor.name,
                        backgroundColor = Color(appliedPalette.nightDark),
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
                            getSampleTaskWithProject(),
                            getSampleTaskWithProject()
                        ),
                        modifier = Modifier.padding(start = 10.dp, bottom = 20.dp),
                        textColor = Color.White,
                        backgroundColor = Color(appliedPalette.nightDark)
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
                            color = Color(appliedPalette.dayLight),
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
                        role = EnumRoles.Admin.name,
                        backgroundColor = Color(appliedPalette.dayDark),
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
                            getSampleTaskWithProject(),
                            getSampleTaskWithProject(),
                        ),
                        modifier = Modifier.padding(start = 10.dp, bottom = 20.dp),
                        textColor = Color.Black,
                        backgroundColor = Color(appliedPalette.dayDark)
                    )

                }

            }


            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Dark",
                    color = getTextColor(),
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
                    color = getTextColor(),
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

        }


        Spacer(modifier = Modifier.weight(1f))

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

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(palettes) { palette ->
                ColorPalettes(
                    modifier = Modifier,
                    palette = palette,
                    onSelectPalette = {
                        appliedPalette = palette
                    },
                    appliedPalette = appliedPalette
                )
            }

        }


        Spacer(modifier = Modifier.weight(1f))

        /**
         *Apply button
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
                    onClick = {
                        onSelectColorPalette(appliedPalette)

                    }
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            androidx.compose.material.Text(
                text = "Apply",
                color = Color.White,
                fontFamily = FontFamily(Nunito.Bold.font),

                )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                Icons.Default.ColorLens,
                contentDescription = "LogoUt Button",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewThemeChooserView() {
    ThemeChooserView(
        onClose = {},
        palettes = listOf(
            getSampleColorPalette(),
            getSampleColorPalette(),
            getSampleColorPalette(),
            getSampleColorPalette(),
            getSampleColorPalette()
        ),
        onSelectColorPalette = {}
    )
}