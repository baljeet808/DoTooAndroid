package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.baljeet.youdotoo.common.getSampleProjectWithTaskCount
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.createproject.components.createProjectView
import com.baljeet.youdotoo.presentation.ui.dotoo.components.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.projects.ProjectsState
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projectsState: ProjectsState,
    userId: String,
    userName: String,
    isUserAPro: Boolean,
    onToggleTask: (DoTooItem) -> Unit,
    navigateToTask: (DoTooItem) -> Unit
) {

    val transition = rememberInfiniteTransition()

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
        initialValue =160.dp,
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


    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            createProjectView(
                modifier = Modifier
                    .fillMaxWidth(),
                sheetState = sheetState
            )
        },
        modifier = Modifier
    ) {

        Box(modifier = Modifier
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
                    color = NightTransparentWhiteColor,
                    radius = 230.dp.toPx(),
                    center = Offset(
                        x = offsetX1.toPx(),
                        y = offsetY1.toPx()
                    )
                )
            })

            Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
                drawCircle(
                    color = NightTransparentWhiteColor,
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
                            scope.launch {
                                sheetState.expand()
                            }
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


                /**
                 * Simple Projects heading
                 * **/
                Text(
                    text = "Projects".uppercase(),
                    color = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    },
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )

                /**
                 * Horizontal list of all projects
                 * **/
                ProjectsLazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    offlineProjects = projectsState.offlineProjects,
                    onlineProjects = projectsState.onlineProjects,
                    navigateToDoToos = navigateToDoToos,
                    userId = userId,
                    isUserAPro = isUserAPro
                )

                /**
                 * Simple Today's Tasks heading
                 * **/
                Text(
                    text = "Today's Tasks".uppercase(),
                    color = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    },
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                    letterSpacing = TextUnit(2f, TextUnitType.Sp)
                )

                DoTooItemsLazyColumn(
                    doToos = projectsState.todayTasks,
                    onToggleDoToo = { doToo ->
                        onToggleTask(doToo)
                    },
                    onNavigateClick = { doToo ->
                        navigateToTask(doToo)
                    },
                    modifier = Modifier,
                    lazyListState = LazyListState()
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
        projectsState = ProjectsState(
            offlineProjects = arrayListOf(
                getSampleProjectWithTaskCount()
            )
        ),
        userId = "",
        isUserAPro = true,
        userName = "Karandeep Kaur",
        onToggleTask = {},
        navigateToTask = {}
    )
}
