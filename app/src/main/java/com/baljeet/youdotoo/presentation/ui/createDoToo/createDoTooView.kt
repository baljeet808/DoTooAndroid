package com.baljeet.youdotoo.presentation.ui.createDoToo

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ModeEdit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baljeet.youdotoo.models.Project
import com.baljeet.youdotoo.models.ProjectWithProfiles
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooPink
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.shared.views.bottomSheets.DueDatesSheet
import com.baljeet.youdotoo.shared.views.bottomSheets.PrioritySheet
import com.baljeet.youdotoo.ui.theme.*
import com.baljeet.youdotoo.util.*
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun createDoTooView(
    navigator: DestinationsNavigator?,
    project: ProjectWithProfiles
) {

    val viewModel  = viewModel<CreateDoTooViewModel>()

    var currentBottomSheet: BottomSheetType? by remember {
        mutableStateOf(null)
    }

    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.hide()
        }
    }
    val openSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.expand()
        }
    }

    var taskName by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var priority by remember {
        mutableStateOf(Priorities.HIGH)
    }
    var dueDate by remember {
        mutableStateOf(
           // DueDates.valueOf(SharedPref.previouslyUsedDueDateOption)
            DueDates.INDEFINITE
        )
    }
    var customDatetime: LocalDateTime by remember {
        mutableStateOf(
            java.time.LocalDateTime.now().toKotlinLocalDateTime()
        )
    }
    val clockState = com.maxkeppeker.sheets.core.models.base.rememberSheetState()
    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            customDatetime = LocalDateTime(
                year = customDatetime.year,
                monthNumber = customDatetime.monthNumber,
                dayOfMonth = customDatetime.dayOfMonth,
                hour = hours,
                minute = minutes
            )
        }
    )

    LaunchedEffect(key1 = true ){
        viewModel.createState.collectLatest {
            if(it.isSuccessful == true){
                navigator?.popBackStack()
            }
        }
    }

    val calendarState = com.maxkeppeker.sheets.core.models.base.rememberSheetState()

    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            disabledTimeline = CalendarTimeline.PAST,
        ),
        properties = DialogProperties(
            dismissOnBackPress = true
        ),
        selection = CalendarSelection.Date {date ->
            customDatetime = LocalDateTime(
                year = date.year,
                monthNumber = date.monthValue,
                dayOfMonth = date.dayOfMonth,
                hour = 0,
                minute = 0
            )
            clockState.show()
        }
    )

    BottomSheetScaffold(
        sheetContent = {
            currentBottomSheet?.let {
                when (it) {
                    BottomSheetType.TYPE1 -> {
                        PrioritySheet(priority = priority) { newPriority ->
                            priority = newPriority
                            closeSheet()
                        }
                    }
                    BottomSheetType.TYPE2 -> {
                        DueDatesSheet(
                            dueDate = dueDate,
                            onDateSelected = { selectedDate ->
                                dueDate = selectedDate
                               // SharedPref.previouslyUsedDueDateOption = selectedDate.name
                                customDatetime = selectedDate.getExactDateTime()
                                closeSheet()
                            },
                            onDatePickerSelected = {
                                //OpenDatePicker
                                calendarState.show()
                                dueDate = DueDates.CUSTOM
                                closeSheet()
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        )
        {

            Column(modifier = Modifier.fillMaxSize()) {

                /**
                 * Top Label of screen
                 * **/
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create New DoToo",
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Nunito.ExtraBold.font),
                        fontSize = 28.sp
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))


                /**
                 * Describe you task section
                 * **/
                Text(
                    text = "Describe Your Task",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 18.sp,
                    color = getTextColor(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = getCardColor(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(all = 10.dp)
                ) {

                    OutlinedTextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = {
                            Text(
                                text = "Give it a name",
                                color = Color.Gray
                            )
                        },
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text(
                                text = "Any note about this? (Optional)",
                                color = Color.Gray
                            )
                        },
                        maxLines = 3,
                        singleLine = false,
                        minLines = 1,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                currentBottomSheet = BottomSheetType.TYPE1
                                openSheet()
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))


                /**
                 * Select priority section
                 * **/
                Text(
                    text = "Select Priority",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 18.sp,
                    color = getTextColor(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = getCardColor(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(all = 10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = priority.toString,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.weight(0.9f)
                        )

                        IconButton(
                            onClick = {
                                currentBottomSheet = BottomSheetType.TYPE1
                                openSheet()
                            },
                            modifier = Modifier.weight(.1f)
                        ) {
                            Icon(
                                Icons.Default.ModeEdit,
                                contentDescription = "Change Priority Button",
                                tint = if (isSystemInDarkTheme()) {
                                    DotooPink
                                } else {
                                    DotooBlue
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                /**
                 * Select due date section
                 * **/
                Text(
                    text = "Select Due Date",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 18.sp,
                    color = getTextColor(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = getCardColor(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(all = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = dueDate.toString,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily(Nunito.SemiBold.font),
                            fontSize = 18.sp,
                            modifier = Modifier.weight(.9f)
                        )
                        IconButton(
                            onClick = {
                                currentBottomSheet = BottomSheetType.TYPE2
                                openSheet()
                            },
                            modifier = Modifier.weight(.1f)
                        ) {
                            Icon(
                                Icons.Default.ModeEdit,
                                contentDescription = "Change Due Date Button",
                                tint = if (isSystemInDarkTheme()) {
                                    DotooPink
                                } else {
                                    DotooBlue
                                }
                            )
                        }
                    }
                    if(dueDate != DueDates.INDEFINITE) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = customDatetime.toNiceDateTimeFormat(),
                                color = MaterialTheme.colorScheme.secondary,
                                fontFamily = FontFamily(Nunito.SemiBold.font),
                                fontSize = 18.sp,
                                modifier = Modifier.weight(.9f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))


                /**
                 * Create button
                 * **/
                Button(
                    onClick = {
                        //do something here
                              viewModel.createDoToo(
                                  project = project.project,
                                  name = taskName,
                                  description = description,
                                  priority = priority,
                                  dueDate = dueDate,
                                  customDate = null
                              )
                    },
                    colors = ButtonDefaults
                        .buttonColors(containerColor = if(isSystemInDarkTheme()){
                            DotooPink
                        } else
                            DotooBlue
                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .padding(top = 20.dp, start = 0.dp, end = 0.dp),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text="Create",
                        color = Color.White,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontSize = 18.sp
                    )
                }
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun createDoTooPreview() {
    createDoTooView(
        navigator = null,
        project = ProjectWithProfiles(
            project = Project(
                id = "74D46CEC-04C8-4E7E-BA2E-B9C7E8D2E958",
                name = "Test is the name",
                description = "Android is my game. Because test is my name",
                ownerId = "5JULxCTJWVM04kzeGrbs4DJRzsS2",
                collaboratorIds = listOf(
                    "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    "NuZXwLl3a8O3mXRcXFsJzHQgB172"
                ),
                viewerIds = listOf()
            ),
            profiles = listOf()
        )
    )
}
