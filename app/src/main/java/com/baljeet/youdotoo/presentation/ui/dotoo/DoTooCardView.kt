package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.presentation.ui.theme.DotooGreen
import com.baljeet.youdotoo.presentation.ui.theme.getCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getOnCardColor
import com.baljeet.youdotoo.presentation.ui.theme.getOppositeOnCardColor
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.theme.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun DoTooCardView(
    doToo: DoTooWithProfiles,
    onTapChat: () -> Unit,
    onToggleDone: (Boolean) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTapChat)
            .background(
                color = if (doToo.doToo.done) {
                    getCardColor()
                } else {
                    getCardColor()
                },
                shape = RoundedCornerShape(8.dp)
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = getOnCardColor(),
                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = doToo.doToo.title,
                    color = if (doToo.doToo.done) {
                        if(isSystemInDarkTheme()){
                            Color.Gray
                        }else{
                            Color.Black
                        }
                    } else {
                        //getOppositeOnCardColor()
                        MaterialTheme.colorScheme.secondary
                    },
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 20.sp,
                    style = if (doToo.doToo.done) {
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    } else {
                        TextStyle()
                    },
                    modifier = Modifier
                        .weight(0.9f)
                        .padding(start = 10.dp, bottom = 5.dp)
                )

                Checkbox(
                    checked = doToo.doToo.done,
                    onCheckedChange = onToggleDone,
                    modifier = Modifier
                        .weight(0.1f)
                        .padding(end = 10.dp, bottom = 5.dp, top = 5.dp),
                    colors = CheckboxDefaults.colors(
                        checkedColor = DotooGreen,
                        checkmarkColor = MaterialTheme.colorScheme.background,
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )
            {
                Text(
                    text = doToo.doToo.description,
                    color = if (doToo.doToo.done) {
                        if(isSystemInDarkTheme()){
                            Color.Gray
                        }else{
                            Color.Gray
                            //OnDotooDarkGray
                        }
                    } else {
                        //Color.Gray
                        getOppositeOnCardColor()
                    },
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp,
                    /*style = if (doToo.doToo.done) {
                        TextStyle(textDecoration = TextDecoration.LineThrough)
                    } else {
                        TextStyle()
                    },*/
                    modifier = Modifier.padding(top = 5.dp, end = 10.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                val dueDate = try {
                    Instant.fromEpochMilliseconds(doToo.doToo.dueDate * 1000)
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                } catch (e: java.lang.Exception) {
                    null
                }
                dueDate?.let { date ->
                    Text(
                        text = "" + date.month.name.substring(0, 3)
                            .replaceFirstChar { c -> c.uppercase() }
                                + " " + date.dayOfMonth + ", " + date.year + " at " + date.hour + ":" + date.minute,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        color = Color.Gray
                    )
                }

                /* IconButton(
                     onClick = onTapChat,
                     modifier = Modifier
                         .height(26.dp)
                         .width(26.dp),
                 ) {
                     Icon(
                         Icons.Default.Chat,
                         contentDescription = "Navigate to list button",
                         tint = DotooBlue,
                         modifier = Modifier
                             .width(22.dp)
                             .height(22.dp)
                     )
                 }*/

            }
        }
    }

}