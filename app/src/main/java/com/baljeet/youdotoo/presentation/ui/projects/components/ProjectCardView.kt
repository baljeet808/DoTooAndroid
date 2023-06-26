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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.profilesLazyRow
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor


@Composable
fun ProjectCardView(
    project: ProjectWithProfiles,
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
                    text = project.project.name,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 20.sp
                )

               /* FilledIconButton(
                    onClick = onItemClick,
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(20.dp),
                            clip = true,
                        )
                        .height(26.dp)
                        .width(26.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = DotooGray
                    )
                ) {
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Navigate to list button",
                        tint = getThemeColor(),
                        modifier = Modifier
                            .width(20.dp)
                            .height(20.dp)
                    )
                }*/
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = project.project.description,
                    color = Color.Gray,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp
                )
            }
            project.profiles?.let { profiles ->
                profilesLazyRow(profiles = profiles, onTapProfiles = { })
            }
        }
    }

}
/*
@Preview(showBackground = true)
@Composable
fun DefaultProjectCardPreview() {
    ProjectCardView(
        project = ,
        onItemClick = {}
    )
}*/
