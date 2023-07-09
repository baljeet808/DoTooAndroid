package com.baljeet.youdotoo.presentation.ui.create_task

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.datetime.LocalDateTime

@Composable
fun CreateTaskView(
    createTask: (
        name: String,
        description: String,
        priority: Priorities,
        dueDate: DueDates,
        customDate: LocalDateTime?,
    ) -> Unit,
    allProjects: List<Project>,
    projectId: String,
    navigateBack: () -> Unit
) {

    var dueDate by remember {
        mutableStateOf(
            DueDates.NEXT_FRIDAY
        )
    }

    var selectedProject by remember {
        mutableStateOf(
            allProjects.firstOrNull { project -> project.id == projectId }
        )
    }

    var priority by remember {
        mutableStateOf(
            Priorities.HIGH
        )
    }

    var descriptionOn by remember {
        mutableStateOf(false)
    }

    var description by remember {
        mutableStateOf("")
    }

    var title by remember {
        mutableStateOf("")
    }


    Column(
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooNormalBlue
                } else {
                    DotooGray
                }
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        /**
         * Row for top close button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End
        ) {

            IconButton(
                onClick = navigateBack,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(40.dp)
                    )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Button to close side drawer.",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        Color.Black
                    }
                )
            }
        }

        /**
         * Row for Due date and Project selection
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //Due Date
            Row(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = "Button to set due date for this task.",
                    tint = LightAppBarIconsColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = dueDate.toString,
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            //Select Project
            Row(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Adjust,
                    contentDescription = "Button to set due date for this task.",
                    tint = Color(selectedProject?.color ?: 0xFFFF8526)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedProject?.name ?: "",
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        Spacer(modifier = Modifier.height(20.dp))

        /**
         * Row for dotoo additional fields
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(
                40.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //set priority
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.LowPriority,
                    contentDescription = "Button to set priority",
                    tint = LightAppBarIconsColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = priority.toString,
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //toggle description
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            descriptionOn = descriptionOn.not()
                            if (descriptionOn.not()) {
                                description = ""
                            }
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (descriptionOn) {
                        Icons.Outlined.PlaylistRemove
                    } else {
                        Icons.Outlined.Notes
                    },
                    contentDescription = "Button to set due date for this task.",
                    tint = LightAppBarIconsColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (descriptionOn) {
                        "Clear Description"
                    } else {
                        "Add Description"
                    },
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }


        }

        Spacer(modifier = Modifier.height(20.dp))
        /**
         * Text field for adding title
         * **/
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(
                1.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Title",
                color = LightAppBarIconsColor,
                fontSize = 12.sp,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
            TextField(
                value = title,
                onValueChange = {
                    if (it.length <= maxTitleCharsAllowed) {
                        title = it
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Enter new task",
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            Color.Black
                        },
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font)
                    )
                },
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) {
                        DotooGray
                    } else {
                        Color.Black
                    },
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font)
                ),
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),

            )
            Text(
                text = "${title.length}/$maxTitleCharsAllowed",
                color = if (title.length >= maxTitleCharsAllowed) {
                    DoTooRed
                } else {
                    LightAppBarIconsColor
                },
                fontSize = 12.sp,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        AnimatedVisibility(visible = descriptionOn) {
            Spacer(modifier = Modifier.height(40.dp))
        }

        /**
         * Text field for adding description
         * **/
        AnimatedVisibility(visible = descriptionOn) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(
                    1.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Description",
                    color = LightAppBarIconsColor,
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                )
                TextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= maxDescriptionCharsAllowed) {
                            description = it
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Enter description here",
                            color = if (isSystemInDarkTheme()) {
                                DotooGray
                            } else {
                                Color.Black
                            },
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Nunito.SemiBold.font)
                        )
                    },
                    textStyle = TextStyle(
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            Color.Black
                        },
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font)
                    ),
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),

                    )
                Text(
                    text = "${description.length}/$maxDescriptionCharsAllowed",
                    color = if (description.length >= maxDescriptionCharsAllowed) {
                        DoTooRed
                    } else {
                        LightAppBarIconsColor
                    },
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    modifier = Modifier.padding(start = 15.dp)
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        /**
         * Save button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End
        ){

            Row(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(30.dp))
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            NightDotooBrightBlue
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
            ) {
                Text(
                    text = "New Task",
                    color = if (isSystemInDarkTheme()) {
                        NightDotooBrightBlue
                    } else {
                        Color.White
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.ExpandLess,
                    contentDescription = "Create task button",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooBrightBlue
                    } else {
                        Color.White
                    }
                )
            }

        }

    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewCreateTaskView() {
    val sampleProject = getSampleProject()
    CreateTaskView(
        createTask = { _, _, _, _, _ -> },
        navigateBack = {},
        allProjects = listOf(
            sampleProject,
            getSampleProject(),
            getSampleProject(),
        ),
        projectId = sampleProject.id
    )
}