package com.baljeet.youdotoo.presentation.ui.project

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getSampleProjectWithEverything
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithEveryThing
import com.baljeet.youdotoo.presentation.ui.dotoo.components.DoTooItemsLazyColumn
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles
import com.baljeet.youdotoo.presentation.ui.theme.DoTooYellow
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue

@Composable
fun ProjectView(
    project: ProjectWithEveryThing,
    onToggle : (doTooItem : DoTooItem, project : Project ) -> Unit,
    navigateToCreateTask : () -> Unit
) {

    val lazyListState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCreateTask,
                modifier = Modifier,
                backgroundColor =
                if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    DoTooYellow
                }
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = "Floating button to quickly add a task to this project",
                    tint = Color.White
                )
            }
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDotooNormalBlue
                    } else {
                        DotooGray
                    }
                ),
            verticalArrangement = Arrangement.Top
        ) {

            /**
             *Top Project Card
             * **/
            ProjectCardWithProfiles(
                project = project,
                lazyListState = lazyListState
            )

            /**
             * List of tasks form this project
             * **/
            DoTooItemsLazyColumn(
                lazyListState = lazyListState,
                doToos = project.doToos,
                onToggleDoToo = {doToo->
                    project.project?.let {project ->
                        onToggle(doToo, project)
                    }
                },
                onNavigateClick = {},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp)
            )

        }
    }



}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectView() {
    ProjectView(
        project = getSampleProjectWithEverything(),
        onToggle = {_,_->},
        navigateToCreateTask = {}
    )
}