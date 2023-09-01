package com.baljeet.youdotoo.presentation.ui.create_task

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.Adjust
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LowPriority
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.PlaylistRemove
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.EnumCreateTaskSheetType
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.addHapticFeedback
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.maxDescriptionCharsAllowed
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.common.toNiceDateFormat
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.data.mappers.toProjectEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.bottomSheets.DueDatesSheet
import com.baljeet.youdotoo.presentation.ui.shared.views.bottomSheets.PrioritySheet
import com.baljeet.youdotoo.presentation.ui.shared.views.bottomSheets.SelectProjectBottomSheet
import com.baljeet.youdotoo.presentation.ui.theme.DoTooRed
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.LightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooBrightBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooFooterTextColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getDayDarkColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getNightDarkColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun UpsertTaskView(
    createTask: (
        name: String,
        description: String,
        priority: Priorities,
        dueDate: DueDates,
        customDate: LocalDate?,
        selectedProject: Project
    ) -> Unit,
    projectId: String,
    navigateBack: () -> Unit,
    projects: List<ProjectEntity>
) {

    SharedPref.init(LocalContext.current)

    val focusScope = rememberCoroutineScope()

    val composedForFirstTime = remember {
        mutableStateOf(true)
    }

    val keyBoardController = LocalSoftwareKeyboardController.current
    val titleFocusRequester = remember {
        FocusRequester()
    }
    val descriptionFocusRequester = remember {
        FocusRequester()
    }

    val hapticFeedback = LocalHapticFeedback.current

    val transition = rememberInfiniteTransition(label = "")


    val rotation = transition.animateValue(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Float.VectorConverter, label = ""
    )

    var showTitleErrorAnimation by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = showTitleErrorAnimation) {
        delay(1000)
        showTitleErrorAnimation = false
    }

    var currentBottomSheet: EnumCreateTaskSheetType? by remember {
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


    var dueDate by remember {
        mutableStateOf(
            DueDates.TODAY
        )
    }
    var customDatetime: LocalDate? by remember {
        mutableStateOf(
            null
        )
    }

    var selectedProject by remember {
        mutableStateOf(
            projects.firstOrNull { project -> project.id == projectId }
        )
    }

    if (projects.isNotEmpty()) {
        selectedProject = projects.firstOrNull { project -> project.id == projectId }
    }

    var priority by remember {
        mutableStateOf(
            Priorities.HIGH
        )
    }

    var descriptionOn by remember {
        mutableStateOf(true)
    }

    var description by remember {
        mutableStateOf("")
    }

    var title by remember {
        mutableStateOf("")
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
        selection = CalendarSelection.Date { date ->
            customDatetime = LocalDate(
                year = date.year,
                monthNumber = date.monthValue,
                dayOfMonth = date.dayOfMonth
            )
        }
    )



    BottomSheetScaffold(
        sheetContent = {
            currentBottomSheet?.let {
                when (it) {
                    EnumCreateTaskSheetType.SELECT_PRIORITY -> {
                        PrioritySheet(priority = priority) { newPriority ->
                            priority = newPriority
                            closeSheet()
                        }
                    }

                    EnumCreateTaskSheetType.SELECT_DUE_DATE -> {
                        DueDatesSheet(
                            dueDate = dueDate,
                            onDateSelected = { selectedDate ->
                                dueDate = selectedDate
                                customDatetime = selectedDate.getExactDate()
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

                    EnumCreateTaskSheetType.SELECT_PROJECT -> {
                        SelectProjectBottomSheet(
                            projects = projects.map { p -> p.toProject() },
                            selectedProject = selectedProject?.toProject(),
                            onProjectSelected = { project ->
                                selectedProject = project.toProjectEntity()
                                closeSheet()
                            }
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) {
                    getNightDarkColor()
                } else {
                    getDayDarkColor()
                },
                shape = RoundedCornerShape(20.dp)
            ),
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        /**
         * Main content
         * **/
        Column(
            modifier = Modifier
                .background(
                    color = getLightThemeColor()
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                Text(
                    text = "Create Task",
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 28.sp,
                    color = getTextColor()
                )


                Spacer(modifier = Modifier.weight(.5f))

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
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .clickable(
                            onClick = {
                                closeSheet()
                                keyBoardController?.hide()
                                currentBottomSheet = EnumCreateTaskSheetType.SELECT_DUE_DATE
                                openSheet()
                            }
                        ),
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
                        .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                        .clickable(
                            onClick = {
                                closeSheet()
                                keyBoardController?.hide()
                                currentBottomSheet = EnumCreateTaskSheetType.SELECT_PROJECT
                                openSheet()
                            }
                        ),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Adjust,
                        contentDescription = "Button to set due date for this task.",
                        tint = Color(selectedProject?.color ?: 0xFFFF8526),
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
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


            AnimatedVisibility(visible = (customDatetime != null)) {
                Text(
                    text = "Due Date set to ".plus(
                        customDatetime?.toJavaLocalDate()?.toNiceDateFormat() ?: ""
                    ),
                    color = Color(selectedProject?.color ?: 4294935846),
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 30.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            /**
             * Row for dotoo additional fields
             * **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                //set priority
                Row(
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                closeSheet()
                                keyBoardController?.hide()
                                currentBottomSheet = EnumCreateTaskSheetType.SELECT_PRIORITY
                                openSheet()
                            }
                        ),
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
                                if (descriptionOn) {
                                    closeSheet()
                                    focusScope.launch {
                                        delay(300)
                                        keyBoardController?.show()
                                        descriptionFocusRequester.requestFocus()
                                    }
                                } else {
                                    closeSheet()
                                    description = ""
                                    descriptionFocusRequester.freeFocus()
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
                    fontSize = 13.sp,
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
                            color = getTextColor(),
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Nunito.SemiBold.font),
                            modifier = Modifier
                                .rotate(
                                    if (showTitleErrorAnimation) {
                                        rotation.value
                                    } else {
                                        0f
                                    }
                                )
                        )
                    },
                    textStyle = TextStyle(
                        color = getTextColor(),
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font)
                    ),
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(titleFocusRequester)
                        .onFocusEvent {
                            if (it.hasFocus) {
                                closeSheet()
                            }
                        },
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (descriptionOn) {
                            ImeAction.Next
                        } else {
                            ImeAction.Done
                        }
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            descriptionFocusRequester.requestFocus()
                        },
                        onDone = {
                            if (title.isNotBlank()) {
                                createTask(
                                    title,
                                    description,
                                    priority,
                                    dueDate,
                                    customDatetime,
                                    selectedProject!!.toProject()
                                )
                            } else {
                                title = ""
                                showTitleErrorAnimation = true
                            }
                            addHapticFeedback(hapticFeedback = hapticFeedback)
                        }
                    )
                )
                Text(
                    text = "${title.length}/$maxTitleCharsAllowed",
                    color = if (title.length >= maxTitleCharsAllowed) {
                        DoTooRed
                    } else {
                        LightAppBarIconsColor
                    },
                    fontSize = 13.sp,
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
                        fontSize = 13.sp,
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
                                color = getTextColor(),
                                fontSize = 16.sp,
                                fontFamily = FontFamily(Nunito.SemiBold.font)
                            )
                        },
                        textStyle = TextStyle(
                            color = getTextColor(),
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Nunito.SemiBold.font)
                        ),
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(descriptionFocusRequester)
                            .onFocusEvent {
                                if (it.hasFocus) {
                                    closeSheet()
                                }
                            },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (title.isNotBlank()) {
                                    createTask(
                                        title,
                                        description,
                                        priority,
                                        dueDate,
                                        customDatetime,
                                        selectedProject!!.toProject()
                                    )
                                } else {
                                    title = ""
                                    showTitleErrorAnimation = true
                                }
                                addHapticFeedback(hapticFeedback = hapticFeedback)
                            }
                        )
                    )
                    Text(
                        text = "${description.length}/$maxDescriptionCharsAllowed",
                        color = if (description.length >= maxDescriptionCharsAllowed) {
                            DoTooRed
                        } else {
                            LightAppBarIconsColor
                        },
                        fontSize = 13.sp,
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
            ) {

                Row(
                    modifier = Modifier
                        .shadow(elevation = 5.dp, shape = RoundedCornerShape(30.dp))
                        .background(
                            color = selectedProject?.color?.let { Color(it) }
                                ?: NightDotooBrightBlue,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
                        .clickable(
                            onClick = {
                                if (title.isNotBlank()) {
                                    createTask(
                                        title,
                                        description,
                                        priority,
                                        dueDate,
                                        customDatetime,
                                        selectedProject!!.toProject()
                                    )
                                } else {
                                    title = ""
                                    showTitleErrorAnimation = true
                                }
                                addHapticFeedback(hapticFeedback = hapticFeedback)
                            }
                        )
                ) {
                    Text(
                        text = "New Task",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.ExpandLess,
                        contentDescription = "Create task button",
                        tint = Color.White
                    )
                }
            }

            LaunchedEffect(composedForFirstTime ){
                keyBoardController?.show()
                delay(500)
                titleFocusRequester.requestFocus()
                composedForFirstTime.value = false
            }

        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewUpsertTaskView() {
    val sampleProject = getSampleProject()
    UpsertTaskView(
        createTask = { _, _, _, _, _, _ -> },
        navigateBack = {},
        projectId = sampleProject.id,
        projects = listOf()
    )
}