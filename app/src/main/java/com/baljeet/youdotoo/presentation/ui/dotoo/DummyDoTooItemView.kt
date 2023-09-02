package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito

@Composable
fun DummyDoTooItemView(
    doToo: TaskWithProject,
    textColor : Color = Color.White,
    backgroundColor : Color = Color(getRandomColor())
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(top = 5.dp, bottom = 8.dp)
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(
                     topEnd = 0.dp,
                    topStart = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 0.dp
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 5.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            IconButton(
                onClick = {},
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(0.dp),
            ) {
                if (doToo.task.done) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Checked circular icon",
                        tint = Color(doToo.projectEntity.color),
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = "Checked circular icon",
                        tint = Color(doToo.projectEntity.color),
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = doToo.task.title,
                color = textColor,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = if (doToo.task.done) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                },
                modifier = Modifier
                    .weight(0.9f)
            )

        }

    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDummyDoTooItemView() {
    DummyDoTooItemView(
        doToo = getSampleTaskWithProject()
    )
}