package com.baljeet.youdotoo.presentation.ui.projects.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.baljeet.youdotoo.common.getSampleProjectWithEverything
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.models.ProjectWithEveryThing
import com.baljeet.youdotoo.domain.models.User

@Composable
fun ProjectView(
    project : ProjectWithEveryThing,
) {



}



@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectView(){
    ProjectView(
        project = getSampleProjectWithEverything()
    )
}