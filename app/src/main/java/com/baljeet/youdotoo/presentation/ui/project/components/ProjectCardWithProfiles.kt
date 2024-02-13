package com.baljeet.youdotoo.presentation.ui.project.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
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
import com.baljeet.youdotoo.common.EnumRoles
import com.baljeet.youdotoo.common.getColor
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.common.maxDescriptionCharsAllowed
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.dialogs.AppCustomDialog
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBox
import com.baljeet.youdotoo.presentation.ui.shared.views.lazies.ProfilesLazyRow
import com.baljeet.youdotoo.presentation.ui.theme.DoTooYellow
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor

@Composable
fun ProjectCardWithProfiles(
    project: Project,
    users: List<User>,
    tasks: List<TaskEntity>,
    onItemDeleteClick: () -> Unit,
    updateProjectTitle: (title: String) -> Unit,
    updateProjectDescription: (title: String) -> Unit,
    toggleNotificationSetting: () -> Unit,
    onClickInvite: () -> Unit,
    showFullCardInitially : Boolean = true,
    showDialogBackgroundBlur : (showBlur : Boolean) -> Unit
) {

    val role = getRole(project)

    var showAll by remember {
        mutableStateOf(showFullCardInitially)
    }

    val showViewerPermissionDialog = remember {
        mutableStateOf(false)
    }

    var showEditTitleBox by remember {
        mutableStateOf(false)
    }
    var showEditDescriptionBox by remember {
        mutableStateOf(false)
    }

    if (showViewerPermissionDialog.value){
        AppCustomDialog(
            onDismiss = {
                showDialogBackgroundBlur(false)
                showViewerPermissionDialog.value = false
            },
            onConfirm = {
                showDialogBackgroundBlur(false)
                showViewerPermissionDialog.value = false
            },
            title = "Permission Issue! 😣",
            description = "Sorry, only project owner can edit project details.",
            topRowIcon = Icons.Default.Lock,
            onChecked = {  },
            showCheckbox = false,
            modifier = Modifier
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(20.dp))
            .clip(shape = RoundedCornerShape(20.dp))
            .background(color = project.color.getColor()),
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
                color = project.color.getColor(),
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

        AnimatedVisibility(visible = showAll) {
            ProjectTopBar(
                notificationsState = true,
                onNotificationItemClicked = { /*TODO*/ },
                onDeleteItemClicked = onItemDeleteClick,
                onClickInvite = onClickInvite,
                modifier = Modifier,
                role = role
            )
        }

        AnimatedVisibility(visible = showAll) {
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
                AnimatedVisibility(visible = showEditDescriptionBox.not()) {
                    Text(
                        text = project.description.ifBlank {
                            if (role == EnumRoles.ProAdmin || role == EnumRoles.Admin){
                                "Add Description here..."
                            }else{
                                ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = {
                                    if(role == EnumRoles.ProAdmin || role == EnumRoles.Admin) {
                                        showEditDescriptionBox = true
                                    }else{
                                        showDialogBackgroundBlur(true)
                                        showViewerPermissionDialog.value = true
                                    }
                                }
                            )
                            .padding(start = 5.dp, end = 5.dp),
                        color = LessTransparentWhiteColor,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
                    )
                }
                AnimatedVisibility(visible = showEditDescriptionBox) {
                    EditOnFlyBox(
                        modifier = Modifier,
                        onSubmit = { desc ->
                           updateProjectDescription(desc)
                            showEditDescriptionBox = false
                        },
                        placeholder = project.description ,
                        label = "Project Description",
                        maxCharLength = maxDescriptionCharsAllowed,
                        onCancel = {
                            showEditDescriptionBox = false
                        },
                        themeColor = project.color.getColor(),
                        lines = 3
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            AnimatedVisibility(visible = showEditTitleBox.not()) {
                Text(
                    text = project.name.ifBlank {
                        if(role == EnumRoles.ProAdmin || role == EnumRoles.Admin){
                            "Add title here..."
                        }else{
                            "No title yet 🤪"
                        }
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .clickable(
                            onClick = {
                                if(role == EnumRoles.ProAdmin || role == EnumRoles.Admin) {
                                    showEditTitleBox = true
                                }else{
                                    showDialogBackgroundBlur(true)
                                    showViewerPermissionDialog.value = true
                                }
                            }
                        )
                        .fillMaxWidth(),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 38.sp,
                    color = Color.White,
                    lineHeight = TextUnit(49f, TextUnitType.Sp)
                )
            }
            AnimatedVisibility(visible = showEditTitleBox) {
                EditOnFlyBox(
                    modifier = Modifier,
                    onSubmit = { title ->
                        updateProjectTitle(title)
                        showEditTitleBox = false
                    },
                    placeholder = project.name ,
                    label = "Project Title",
                    maxCharLength = maxTitleCharsAllowed,
                    onCancel = {
                        showEditTitleBox = false
                    },
                    themeColor = project.color.getColor(),
                    lines = 2
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tasks.size.toString().plus(" Tasks"),
                    modifier = Modifier
                        .padding(start = 5.dp, end = 5.dp),
                    color = LessTransparentWhiteColor,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
                )

                TextButton(
                    onClick = { showAll = showAll.not() },
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(
                        text = if (showAll) {
                            "Show less"
                        } else {
                            "Show more"
                        },
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = Color.White
                    )
                    Icon(
                        if (showAll) {
                            Icons.Default.ExpandLess
                        } else {
                            Icons.Default.ExpandMore
                        },
                        contentDescription = "show less or more button",
                        tint = Color.White
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectCardWithProfiles() {
    ProjectCardWithProfiles(
        project = getSampleProject(),
        tasks = listOf(
            getSampleDotooItem()
        ),
        users = listOf(
            getSampleProfile()
        ),
        onItemDeleteClick = {},
        updateProjectDescription = {},
        updateProjectTitle = {},
        toggleNotificationSetting = {},
        onClickInvite = {},
        showDialogBackgroundBlur = {}
    )
}