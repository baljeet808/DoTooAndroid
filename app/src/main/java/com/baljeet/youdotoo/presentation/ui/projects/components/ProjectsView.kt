package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProjectWithTasks
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.dotoo.components.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*

@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projects: List<ProjectWithDoToos>,
    todayTasks : List<DoTooItem>,
    userId: String,
    userName: String,
    onToggleTask: (DoTooItem) -> Unit,
    navigateToTask: (DoTooItem) -> Unit,
    navigateToCreateTask: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    val transition = rememberInfiniteTransition()

    val darkTheme = isSystemInDarkTheme()

    val offsetX by transition.animateValue(
        initialValue = (300).dp,
        targetValue = 150.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    val offsetY by transition.animateValue(
        initialValue = (450).dp,
        targetValue = 550.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )

    val offsetX1 by transition.animateValue(
        initialValue = 160.dp,
        targetValue = 310.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    val offsetY1 by transition.animateValue(
        initialValue = 550.dp,
        targetValue = 450.dp,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Dp.VectorConverter
    )
    Scaffold(
        floatingActionButton = {
            androidx.compose.material.FloatingActionButton(
                onClick = navigateToCreateTask,
                modifier = Modifier,
                backgroundColor = NightDotooBrightBlue
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Floating button to quickly add a task to this project",
                    tint = Color.White
                )
            }
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDotooNormalBlue
                    } else {
                        DotooGray
                    }
                )
        ) {


            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = if (darkTheme) {
                        NightTransparentWhiteColor
                    } else {
                        LessTransparentBlueColor
                    },
                    radius = 230.dp.toPx(),
                    center = Offset(
                        x = offsetX1.toPx(),
                        y = offsetY1.toPx()
                    )
                )
            })

            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = if (darkTheme) {
                        NightTransparentWhiteColor
                    } else {
                        LessTransparentBlueColor
                    },
                    radius = 180.dp.toPx(),
                    center = Offset(
                        x = offsetX.toPx(),
                        y = offsetY.toPx()
                    )
                )
            })


            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top
            ) {
                /**
                 * Top Row for greeting and Add project button
                 * **/
                AnimatedVisibility(visible = lazyListState.isScrolled.not()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (userName.length > 8) {
                                "Hi, $userName!"
                            } else {
                                "What's up, $userName!"
                            },
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            fontFamily = FontFamily(Nunito.ExtraBold.font),
                            fontSize = 38.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        FilledIconButton(
                            onClick = {
                                //TODO: navigate to create project screen
                            }, colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = if (isSystemInDarkTheme()) {
                                    NightDotooDarkBlue
                                } else {
                                    NightDotooNormalBlue
                                }
                            ),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)

                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add list button",
                                tint = Color.White
                            )
                        }
                    }

                }

                AnimatedVisibility(visible = lazyListState.isScrolled.not()) {
                    /**
                     * Simple Projects heading
                     * **/
                    Text(
                        text = "Projects".uppercase(),
                        color = LightAppBarIconsColor,
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        letterSpacing = TextUnit(2f, TextUnitType.Sp)
                    )
                }


                /**
                 * Horizontal list of all projects
                 * **/
                AnimatedVisibility(visible = lazyListState.isScrolled.not()) {

                    ProjectsLazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        projects = projects,
                        navigateToDoToos = navigateToDoToos,
                        userId = userId
                    )
                }


                /**
                 * Simple Today's Tasks heading
                 * **/
                Text(
                    text = "Today's Tasks".uppercase(),
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )

                DoTooItemsLazyColumn(
                    doToos = todayTasks,
                    onToggleDoToo = { doToo ->
                        onToggleTask(doToo)
                    },
                    onNavigateClick = { doToo ->
                        navigateToTask(doToo)
                    },
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    lazyListState = lazyListState
                )


            }
        }

    }
}


fun Project.getUserRole(userId: String): String {
    if (this.ownerId == userId) {
        return "Admin"
    }
    if (this.collaboratorIds.contains(userId)) {
        return "Collaborator"
    }
    if (this.viewerIds.contains(userId)) {
        return "Viewer"
    }
    return "Blocked"
}


@Preview(showBackground = true)
@Composable
fun DefaultProjectPreview() {
    ProjectsView(
        navigateToDoToos = {},
        projects= arrayListOf(
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks(),
            getSampleProjectWithTasks()
            ),
        todayTasks = arrayListOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem()
        ),
        userId = "",
        userName = "Karandeep Kaur",
        onToggleTask = {},
        navigateToTask = {},
        navigateToCreateTask = {}
    )
}
