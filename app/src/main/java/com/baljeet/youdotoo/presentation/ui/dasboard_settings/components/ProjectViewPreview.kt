package com.baljeet.youdotoo.presentation.ui.dasboard_settings.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemView
import com.baljeet.youdotoo.presentation.ui.projects.components.ProjectCardView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

@Composable
fun ProjectViewPreview() {

    var showProjectsInitially by remember {
        mutableStateOf(SharedPref.showProjectsInitially)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()

            .padding(20.dp)
            .background(
                color = getDarkThemeColor(),
                shape = RoundedCornerShape(10.dp)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Keep the projects open initially",
                color = getTextColor(),
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Checkbox(
                checked = showProjectsInitially,
                onCheckedChange = {
                    showProjectsInitially = it
                    SharedPref.showProjectsInitially = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(EnumProjectColors.Purple.longValue),
                    uncheckedColor = Color(EnumProjectColors.Purple.longValue)
                )
            )
        }

        val description = buildAnnotatedString {
            val startingString =
                "Want to only see the tasks on dashboard initially? By unchecking this setting you will have to tap "
            val buttonName = "Show projects ⌄"
            val endingString = " to see the projects."

            val buttonStyle = SpanStyle(
                color = Color(EnumProjectColors.Purple.longValue),
                fontSize = 14.sp
            )

            append(startingString)
            withStyle(buttonStyle) {
                append(buttonName)
            }
            append(endingString)
        }


        Text(
            text = description,
            color = Color.Gray,
            fontSize = 14.sp,
            fontFamily = FontFamily(Nunito.Normal.font),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Text(
                text = "Preview",
                color = getTextColor(),
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 16.sp,
                modifier = Modifier
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 10.dp, bottom = 10.dp, end = 10.dp)
                .background(
                    color = getLightThemeColor(),
                    shape = RoundedCornerShape(
                        10.dp
                    )
                )
        ) {


            AnimatedVisibility(visible = showProjectsInitially) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(20.dp))


                    /**
                     * Greeting text
                     * **/
                    Text(
                        text = if (SharedPref.userName.length > 8) {
                            "Hi, ${SharedPref.userName}!"
                        } else {
                            "What's up, ${SharedPref.userName}!"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        fontSize = 26.sp,
                        color = getTextColor()
                    )

                    /**
                     * Top Row for greeting and Add project button
                     * **/
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 20.dp,
                                start = 10.dp,
                                end = 10.dp,
                                bottom = 5.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        /**
                         * Simple Projects heading
                         * **/
                        Text(
                            text = "Projects".uppercase(),
                            color = LightAppBarIconsColor,
                            fontFamily = FontFamily(Nunito.Normal.font),
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(5.dp),
                            letterSpacing = TextUnit(2f, TextUnitType.Sp)
                        )


                        /**
                         * Create Project Button
                         * **/

                        Row(
                            modifier = Modifier
                                .height(25.dp)
                                .background(
                                    color = getDarkThemeColor(),
                                    shape = RoundedCornerShape(60.dp)
                                )
                                .padding(start = 8.dp, end = 8.dp)
                                .clickable(onClick = {}),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Outlined.Add,
                                contentDescription = "Floating button to add a project",
                                tint = getTextColor(),
                                modifier = Modifier
                                    .height(20.dp)
                            )
                            Text(
                                text = "Add Project",
                                fontFamily = FontFamily(Nunito.Normal.font),
                                color = getTextColor(),
                                fontSize = 10.sp
                            )
                        }
                    }

                    /**
                     * Horizontal list of all projects
                     * **/
                    ProjectCardView(
                        modifier = Modifier.padding(15.dp),
                        project = ProjectWithDoToos(
                            project = getSampleProject().toProjectEntity(),
                            tasks = listOf(
                                getSampleDotooItem(),
                                getSampleDotooItem(),
                                getSampleDotooItem()
                            )
                        ),
                        onItemClick = {},
                        role = EnumRoles.Admin,
                        usingForDemo = true
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically

            ) {

                /**
                 * Show less/more button
                 * **/
                TextButton(
                    onClick = {
                        showProjectsInitially = showProjectsInitially.not()
                        SharedPref.showProjectsInitially = SharedPref.showProjectsInitially.not()
                    },
                    modifier = Modifier.padding(start = 5.dp)
                ) {
                    Text(
                        text = if (showProjectsInitially) {
                            "Hide projects"
                        } else {
                            "Show projects"
                        },
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = getTextColor(),
                        fontSize = 12.sp
                    )
                    Icon(
                        if (showProjectsInitially) {
                            Icons.Default.ExpandLess
                        } else {
                            Icons.Default.ExpandMore
                        },
                        contentDescription = "show less or more button",
                        tint = getTextColor(),
                        modifier = Modifier
                            .height(20.dp)
                    )
                }

            }

            AnimatedVisibility(visible = showProjectsInitially.not()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    DoTooItemView(
                        doToo = getSampleTaskWithProject(
                            taskTitle = "Charge up battery bank 🔋"
                        ),
                        navigateToTaskEdit = { },
                        navigateToQuickEditDotoo = { },
                        onToggleDone = {},
                        usingForDemo = true
                    )
                    DoTooItemView(
                        doToo = getSampleTaskWithProject(
                            taskTitle = "Get fruits and snacks 🍇 for the journey",
                            taskDone = false
                        ),
                        navigateToTaskEdit = { },
                        navigateToQuickEditDotoo = { },
                        onToggleDone = {},
                        usingForDemo = true
                    )
                    DoTooItemView(
                        doToo = getSampleTaskWithProject(
                            taskDone = false
                        ),
                        navigateToTaskEdit = { },
                        navigateToQuickEditDotoo = { },
                        onToggleDone = {},
                        usingForDemo = true
                    )
                }
            }

        }

    }
}