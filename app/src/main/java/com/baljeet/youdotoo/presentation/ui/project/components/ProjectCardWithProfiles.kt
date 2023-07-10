package com.baljeet.youdotoo.presentation.ui.project.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectTopBar
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.lazies.ProfilesLazyRow
import com.baljeet.youdotoo.presentation.ui.theme.DoTooYellow
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor


@Composable
fun ProjectCardWithProfiles(
    project: Project?,
    users: List<User>,
    tasks: List<DoTooItem>,
    lazyListState: LazyListState,
) {

    val darkTheme = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    DoTooYellow
                }
            ),
        verticalArrangement = Arrangement.SpaceAround
    ) {


        Canvas(modifier = Modifier.fillMaxWidth(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 230.dp.toPx(),
                center = Offset(
                    x = 40.dp.toPx(),
                    y = 100.dp.toPx()
                )
            )
            drawCircle(
                color = if (darkTheme) {
                    NightDotooDarkBlue
                } else {
                    DoTooYellow
                },
                radius = 100.dp.toPx(),
                center = Offset(
                    x = 50.dp.toPx(),
                    y = 100.dp.toPx()
                )
            )

            //creating lines using canvas
            for (i in 1..6) {
                drawLine(
                    color = NightTransparentWhiteColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(
                        x = (170 + (i * 25)).dp.toPx(),
                        y = (0).dp.toPx()
                    ),
                    end = Offset(
                        x = (160).dp.toPx(),
                        y = (10 + (i * 25)).dp.toPx()
                    )
                )
            }
            for (i in 1..8) {
                drawLine(
                    color = NightTransparentWhiteColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(
                        x = (320 + (i * 25)).dp.toPx(),
                        y = (0).dp.toPx()
                    ),
                    end = Offset(
                        x = (135 + (i * 25)).dp.toPx(),
                        y = (185).dp.toPx()
                    )
                )
            }
        })

        AnimatedVisibility(visible = (lazyListState.isScrolled.not() && (users.isNotEmpty()))) {
            ProjectTopBar(
                notificationsState = true,
                onFavoriteClick = { /*TODO*/ },
                onNotificationItemClicked = { /*TODO*/ },
                onDeleteItemClicked = { /*TODO*/ },
                modifier = Modifier
            )
        }

        AnimatedVisibility(visible = (lazyListState.isScrolled.not() && (users.isNotEmpty()))) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfilesLazyRow(
                    profiles = users,
                    onTapProfiles = {
                        //TODO: show profiles card
                    },
                    visiblePictureCount = 5,
                    imagesWidthAndHeight = 30,
                    spaceBetween = 8,
                    lightColor = DoTooYellow
                )
                Text(
                    text = project?.description?:"",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    color = LessTransparentWhiteColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = project?.name?:"",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = 38.sp,
                color = Color.White,
                lineHeight = TextUnit(49f, TextUnitType.Sp)
            )
            AnimatedVisibility(visible = lazyListState.isScrolled.not()) {
                Text(
                    text = tasks.size.toString().plus(" Tasks"),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    color = LessTransparentWhiteColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectCardWithProfiles() {
    ProjectCardWithProfiles(
        project = getSampleProject(),
        lazyListState = LazyListState(),
        tasks = listOf(
            getSampleDotooItem()
        ),
        users = listOf(
            getSampleProfile()
        )
    )
}