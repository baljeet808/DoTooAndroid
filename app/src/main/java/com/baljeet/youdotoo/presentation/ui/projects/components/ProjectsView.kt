package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.createproject.components.createProjectView
import com.baljeet.youdotoo.presentation.ui.projects.ProjectsState
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import com.baljeet.youdotoo.presentation.ui.theme.getThemeColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsView(
    navigateToDoToos: (project: Project) -> Unit,
    projectsState: ProjectsState,
    userId : String,
    isUserAPro : Boolean
) {
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            createProjectView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                sheetState = sheetState
            )
        },
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSystemInDarkTheme()) {
                        NightDotooNormalBlue
                    } else {
                        Color.White
                    }
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Your DoToos",
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
                    fontFamily = FontFamily(Nunito.ExtraBold.font),
                    fontSize = 38.sp,
                    color = MaterialTheme.colorScheme.secondary
                )

                FilledIconButton(
                    onClick = {
                        scope.launch {
                            sheetState.expand()
                        }
                    }, colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = getCardColor()
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp)

                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add list button",
                        tint = getThemeColor()
                    )
                }
            }


            projectsLazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(10.dp),
                offlineProjects = projectsState.offlineProjects,
                onlineProjects = projectsState.onlineProjects,
                navigateToDoToos = navigateToDoToos,
                userId = userId,
                isUserAPro = isUserAPro
            )
        }
    }
}


@Composable
fun projectsLazyColumn(
    modifier: Modifier,
    userId: String,
    isUserAPro: Boolean,
    offlineProjects: List<Project>,
    onlineProjects: List<Project>,
    navigateToDoToos: (project: Project) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val projectOwned = if (isUserAPro) {
            offlineProjects
        } else {
            onlineProjects.filter { project -> project.ownerId == userId }
        }
        val projectViewing = onlineProjects.filter { project ->
            project.viewerIds.contains(userId)
        }
        val projectSharedToMe = onlineProjects.filter { project ->
            project.collaboratorIds.contains(userId)
        }

        if (projectOwned.isNotEmpty()) {
            item {
                Text(
                    text = "MY DOTOOS",
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    color = getTextColor()
                )
            }
            items(projectOwned) { project ->
                ProjectCardView(
                    project = project,
                    onItemClick = { navigateToDoToos(project) },
                    role = project.getUserRole(userId = userId )
                )
            }
            item {
                Divider(modifier = Modifier.height(20.dp), color = Color.Transparent)
            }
        }
        if (projectSharedToMe.isNotEmpty()) {
            item {
                Text(
                    text = "SHARED WITH ME",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp,
                    color = getTextColor()
                )
            }
            items(projectSharedToMe) { project ->
                ProjectCardView(
                    project = project,
                    onItemClick = { navigateToDoToos(project) },
                    role = project.getUserRole(userId = userId )
                )
            }
            item {
                Divider(modifier = Modifier.height(20.dp), color = Color.Transparent)
            }
        }
        if (projectViewing.isNotEmpty()) {
            item {
                Text(
                    text = "VIEWING",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp,
                    color = getTextColor()
                )
            }
            items(projectViewing) { project ->
                ProjectCardView(
                    project = project,
                    onItemClick = { navigateToDoToos(project) },
                    role = project.getUserRole(userId = userId )
                )
            }
        }
    }
}

fun Project.getUserRole (userId : String): String{
    if(this.ownerId == userId){
        return    "Admin"
    }
    if(this.collaboratorIds.contains(userId)){
       return  "Collaborator"
    }
    if(this.viewerIds.contains(userId)){
       return "Viewer"
    }
    return "Blocked"
}


@Preview(showBackground = true)
@Composable
fun DefaultProjectPreview() {
    ProjectsView(
        navigateToDoToos = {},
        projectsState = ProjectsState(
            offlineProjects = arrayListOf(
                Project(
                    id = "",
                    name = "Home Chores",
                    description = "This project is about the irritating stuff which always gets forgotten.",
                    ownerId = "",
                    collaboratorIds = arrayListOf(),
                    viewerIds = arrayListOf(),
                    update = ""
                )
            )
        ),
        userId = "",
        isUserAPro = true
    )
}
