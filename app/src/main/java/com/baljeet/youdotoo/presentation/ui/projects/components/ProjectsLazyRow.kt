package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getRole
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.local.relations.ProjectWithDoToos
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.Project


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProjectsLazyRow(
    listState : LazyListState,
    modifier: Modifier,
    projects: List<ProjectWithDoToos>,
    navigateToDoToos: (project: Project) -> Unit,
    hideProjectTasksFromDashboard : (project : ProjectEntity) -> Unit
) {

    LazyRow(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(projects, key = {it.project.id}) { project ->
            ProjectCardView(
                modifier = Modifier.animateItemPlacement(
                    animationSpec = tween(
                        durationMillis = 500
                    )
                ),
                project = project,
                onItemClick = { navigateToDoToos(project.project.toProject()) },
                role = getRole(project.project.toProject()),
                hideProjectTasksFromDashboard = { proj ->
                    hideProjectTasksFromDashboard(proj)
                }
            )
        }
    }
}