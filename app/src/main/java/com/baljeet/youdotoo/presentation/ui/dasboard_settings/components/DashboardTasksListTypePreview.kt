package com.baljeet.youdotoo.presentation.ui.dasboard_settings.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LowPriority
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getColor
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.presentation.ui.dotoo.DoTooItemView
import com.baljeet.youdotoo.presentation.ui.dotoo.HeadingTextWithCount
import com.baljeet.youdotoo.presentation.ui.dotoo.TasksPrioritiesTabRow
import com.baljeet.youdotoo.presentation.ui.dotoo.getPrioritiesTabs
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DashboardTasksListTypePreview() {


    SharedPref.init(LocalContext.current)

    var showCalendarViewInitially by remember {
        mutableStateOf(SharedPref.showCalendarViewInitially)
    }



    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box (
            modifier = Modifier
                .background(
                    color = getLightThemeColor()
                )
                .height(1.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Show calendar view initially",
                color = Color.White,
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 16.sp,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Checkbox(
                checked = showCalendarViewInitially,
                onCheckedChange = {
                    showCalendarViewInitially = it
                    SharedPref.showCalendarViewInitially = it
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = EnumProjectColors.Indigo.name.getColor(),
                    uncheckedColor = EnumProjectColors.Indigo.name.getColor()
                ),
                modifier = Modifier.width(25.dp)
            )
        }

        val description = buildAnnotatedString {
            val startingString =
                "Show tasks sorted by due date. Tap button "
            val buttonName = "Calendar View 📆"
            val endingString = " to switch back to priority view."

            val buttonStyle = SpanStyle(
                color = EnumProjectColors.Indigo.name.getColor(),
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
                .padding(start = 40.dp, end = 40.dp, bottom = 10.dp)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 15.dp,
                    bottom = 15.dp,
                    start = 35.dp,
                    end = 35.dp
                )
        ) {
            Text(
                text = "Preview",
                color = Color.White,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 16.sp,
                modifier = Modifier
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(start = 30.dp, bottom = 10.dp, end = 30.dp)
                .background(
                    color = getLightThemeColor(),
                    shape = RoundedCornerShape(
                        10.dp
                    )
                )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically

            ) {

                TextButton(
                    onClick = { showCalendarViewInitially = !showCalendarViewInitially },
                    modifier = Modifier.padding(end = 5.dp)
                ) {
                    Icon(
                        if (showCalendarViewInitially) {
                            Icons.Default.CalendarMonth
                        } else {
                            Icons.Default.LowPriority
                        },
                        contentDescription = "Button to change task list style",
                        tint = getTextColor(),
                        modifier = Modifier
                            .height(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showCalendarViewInitially) {
                            "Calendar View"
                        } else {
                            "Priorities View"
                        },
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = getTextColor(),
                        fontSize = 12.sp
                    )

                }

            }

            AnimatedVisibility(visible = showCalendarViewInitially.not()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    TasksPrioritiesTabRow(
                        pagerState = rememberPagerState(initialPage = 0),
                        tasksTabs = getPrioritiesTabs(
                            filteredTasks = listOf()
                        )
                    )
                    
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

            AnimatedVisibility(visible = showCalendarViewInitially) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    HeadingTextWithCount(
                        heading = "Yesterday",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    DoTooItemView(
                        doToo = getSampleTaskWithProject(
                            taskTitle = "Charge up battery bank 🔋"
                        ),
                        navigateToTaskEdit = { },
                        navigateToQuickEditDotoo = { },
                        onToggleDone = {},
                        usingForDemo = true
                    )

                    HeadingTextWithCount(
                        heading = "Today",
                        modifier = Modifier
                            .fillMaxWidth()
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDashboardTasksListTypePreview(){
    DashboardTasksListTypePreview()
}