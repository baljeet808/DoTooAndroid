package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.addHapticFeedback
import com.baljeet.youdotoo.common.getSampleTaskWithProject
import com.baljeet.youdotoo.common.playWooshSound
import com.baljeet.youdotoo.data.local.relations.TaskWithProject
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DoTooItemView(
    doToo: TaskWithProject,
    navigateToTaskEdit: () -> Unit,
    navigateToQuickEditDotoo : () -> Unit,
    onToggleDone: () -> Unit,
    usingForDemo : Boolean = false
) {

    SharedPref.init(LocalContext.current)
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(if(usingForDemo){ 50.dp }else {80.dp})
            .shadow(elevation = 5.dp, shape = RoundedCornerShape(if(usingForDemo){ 14.dp }else {20.dp}))
            .background(
                color = getDarkThemeColor(),
                shape = RoundedCornerShape(if(usingForDemo){ 14.dp }else {20.dp})
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = navigateToTaskEdit,
                    onLongClick = {
                        addHapticFeedback(hapticFeedback = hapticFeedback)
                        navigateToQuickEditDotoo()
                    }
                )
                .padding(
                    start = if(usingForDemo){ 6.dp }else {10.dp},
                    end = 0.dp,
                    top = if(usingForDemo){ 6.dp }else {10.dp},
                    bottom = if(usingForDemo){ 6.dp }else {10.dp}
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            IconButton(
                onClick = {
                    if (doToo.task.done.not()) {
                        playWooshSound(context)
                    }
                    addHapticFeedback(hapticFeedback = hapticFeedback)
                    onToggleDone()
                },
                modifier = Modifier
                    .height(if(usingForDemo){ 20.dp }else {30.dp})
                    .width(if(usingForDemo){ 20.dp }else {30.dp})
                    .padding(0.dp),
            ) {
                Icon(
                    if (doToo.task.done) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Outlined.Circle
                    },
                    contentDescription = "Checked circular icon",
                    tint = Color(doToo.projectEntity.color),
                    modifier = Modifier
                        .height(if(usingForDemo){ 20.dp }else {30.dp})
                        .width(if(usingForDemo){ 20.dp }else {30.dp})
                )
            }
            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = doToo.task.title,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                },
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = if(usingForDemo){ 13.sp }else {18.sp},
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = if (doToo.task.done) {
                    TextStyle(textDecoration = TextDecoration.LineThrough)
                } else {
                    TextStyle()
                },
                modifier = Modifier
                    .weight(if(usingForDemo){ 0.7f }else {0.9f})
            )

            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription ="Navigate to chat button",
                tint = Color(doToo.projectEntity.color),
                modifier = Modifier
                    .height(if(usingForDemo){ 20.dp }else {30.dp})
                    .width(if(usingForDemo){ 20.dp }else {30.dp})
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewDoTooItemView() {
    DoTooItemView(
        doToo = getSampleTaskWithProject(),
        onToggleDone = {},
        navigateToTaskEdit = {},
        navigateToQuickEditDotoo = {}
    )
}