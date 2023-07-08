package com.baljeet.youdotoo.presentation.ui.create_task

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.datetime.LocalDateTime

@Composable
fun CreateTaskView(
    createTask : (
        name : String,
        description : String,
        priority: Priorities,
        dueDate: DueDates,
        customDate : LocalDateTime?,
    ) -> Unit,
    allProjects : List<Project>,
    navigateBack : () -> Unit
) {

    var dueDate by remember {
        mutableStateOf(
            DueDates.TODAY
        )
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
                        LightDotooFooterTextColor
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
                .padding(start = 20.dp, end = 20.dp)
        ) {

        }

    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCreateTaskView(){
    CreateTaskView(
        createTask = {_,_,_,_,_ ->},
        navigateBack = {},
        allProjects = listOf()
    )
}