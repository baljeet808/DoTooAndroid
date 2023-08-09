package com.baljeet.youdotoo.presentation.ui.edittask

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.common.maxTitleCharsAllowed
import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.data.mappers.toDoTooItemEntity
import com.baljeet.youdotoo.presentation.ui.shared.views.editboxs.EditOnFlyBoxRound
import com.baljeet.youdotoo.presentation.ui.theme.NightDarkThemeColor

@Composable
fun EditTaskView(
    task: DoTooItemEntity?,
    updateTask: (task: DoTooItemEntity, title: String) -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radius = 10.dp
                )
        )
        EditOnFlyBoxRound(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDarkThemeColor
                    } else {
                        Color.White
                    }
                ),
            onSubmit = { title ->
                task?.let {
                    updateTask(task, title)
                }
            },
            placeholder = task?.title?:"",
            label = "Edit Task",
            maxCharLength = maxTitleCharsAllowed,
            onCancel = onClose,
            themeColor = Color(task?.projectColor?: getRandomColor()),
            lines = 2
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditTaskView() {
    EditTaskView(
        task = getSampleDotooItem().toDoTooItemEntity(""),
        updateTask = { _, _ -> },
        onClose = {}
    )
}