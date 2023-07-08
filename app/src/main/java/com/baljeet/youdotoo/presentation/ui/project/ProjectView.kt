package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.baljeet.youdotoo.common.getSampleProjectWithEverything
import com.baljeet.youdotoo.domain.models.ProjectWithEveryThing
import com.baljeet.youdotoo.presentation.ui.project.components.ProjectCardWithProfiles

@Composable
fun ProjectView(
    project: ProjectWithEveryThing,
) {

    val lazyListState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        /**
         *Top Project Card
         * **/
        ProjectCardWithProfiles(
            project = project,
            lazyListState = lazyListState
        )
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectView() {
    ProjectView(
        project = getSampleProjectWithEverything()
    )
}