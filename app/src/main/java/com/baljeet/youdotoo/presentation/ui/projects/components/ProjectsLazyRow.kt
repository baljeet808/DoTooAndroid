package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.projects.ProjectWithTaskCount


@Composable
fun ProjectsLazyRow(
    modifier: Modifier,
    userId: String,
    isUserAPro: Boolean,
    offlineProjects: List<ProjectWithTaskCount>,
    onlineProjects: List<ProjectWithTaskCount>,
    navigateToDoToos: (project: Project) -> Unit
) {

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val projectOwned = if (isUserAPro.not()) {
            offlineProjects
        } else {
            onlineProjects.filter { project -> project.project.ownerId == userId }
        }
        val projectViewing = onlineProjects.filter { project ->
            project.project.viewerIds.contains(userId)
        }
        val projectSharedToMe = onlineProjects.filter { project ->
            project.project.collaboratorIds.contains(userId)
        }
        items(projectOwned) { project ->
            ProjectCardView(
                project = project,
                onItemClick = { navigateToDoToos(project.project) },
                role = project.project.getUserRole(userId = userId )
            )
        }
        items(projectSharedToMe) { project ->
            ProjectCardView(
                project = project,
                onItemClick = { navigateToDoToos(project.project) },
                role = project.project.getUserRole(userId = userId )
            )
        }
        items(projectViewing) { project ->
            ProjectCardView(
                project = project,
                onItemClick = { navigateToDoToos(project.project) },
                role = project.project.getUserRole(userId = userId )
            )
        }
    }
}