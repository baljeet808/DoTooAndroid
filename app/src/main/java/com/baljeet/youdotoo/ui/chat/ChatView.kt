package com.baljeet.youdotoo.ui.chat

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.dotoo.ProjectHelperCard
import com.baljeet.youdotoo.ui.theme.*
import com.baljeet.youdotoo.util.isScrolled
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Updated by Baljeet singh on 18th June, 2023 at 10:00 AM.
 * **/
@Destination
@Composable
fun ChatView(
    navigator: DestinationsNavigator?,
    doToo : DoTooWithProfiles
) {

    val message by remember{
        mutableStateOf("")
    }

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getCardColor())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = doToo.doToo.title,
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = if (doToo.doToo.title.count() < 15) {
                    38.sp
                } else 24.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Checkbox(
                checked = doToo.doToo.done,
                onCheckedChange = {
                    //Todo
                },
                modifier = Modifier
                    .weight(.1f)
                    .height(40.dp)
                    .width(40.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = DotooGreen,
                    checkmarkColor = MaterialTheme.colorScheme.background,
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 200))
                .height(
                    if (lazyListState.isScrolled) {
                        0.dp
                    } else {
                        125.dp
                    }
                )
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Text(
                text = doToo.doToo.description,
                color = Color.Gray,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                fontSize = 16.sp,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            DoTooHelperCard(
                doToo = doToo
            )
        }
    }
}