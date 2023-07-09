package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.Project


@Composable
fun ProjectsLazyRow(
    modifier: Modifier,
    userId: String,
    projects: List<ProjectWithDoToos>,
    navigateToDoToos: (project: Project) -> Unit
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(projects) { project ->
            ProjectCardView(
                project = project,
                onItemClick = { navigateToDoToos(project.project.toProject()) },
                role = project.project.toProject().getUserRole(userId = userId )
            )
        }
    }
}