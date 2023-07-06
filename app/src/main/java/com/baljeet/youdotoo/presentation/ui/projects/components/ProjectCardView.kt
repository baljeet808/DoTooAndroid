package com.baljeet.youdotoo.presentation.ui.projects.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor


@Composable
fun ProjectCardView(
    project: Project,
    onItemClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .background(
                color = getCardColor(),
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = project.name,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 20.sp
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = project.description,
                    color = Color.Gray,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp
                )
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun DefaultProjectCardPreview() {
    ProjectCardView(
        project = Project(
            id = "",
            name = "Home Chores",
            description = "This project is about the irritating stuff which always gets forgotten.",
            ownerId = "",
            collaboratorIds = arrayListOf(),
            viewerIds = arrayListOf(),
            update = ""
        ),
        onItemClick = {}
    )
}
