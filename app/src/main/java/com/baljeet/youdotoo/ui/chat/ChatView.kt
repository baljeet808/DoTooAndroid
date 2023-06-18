package com.baljeet.youdotoo.ui.chat

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.models.*
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.dotoo.ProjectHelperCard
import com.baljeet.youdotoo.ui.theme.*
import com.baljeet.youdotoo.util.SharedPref
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
            .background(color = getCardColor()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        /**
         *Top row about Dotoo name and check box
         * **/
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


        /**
         *Column of description and toolbox for editing this Dotoo
         * **/
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



        val messagesStateList = remember { mutableStateListOf<Message>() }

        /**
         *LazyColumn of Chat
         * **/
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(0.6F),
            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
        ){
            items(messagesStateList){message ->
                MessageBubbleView(
                    message = message,
                    doToo = doToo,
                    onLongPress = {

                    },
                    userId =SharedPref.userId.toString()
                )
            }
        }

        /**
         *SendBox
         * **/
        MessageBoxView(
            onClickSend ={

            }
        ) {

        }

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewChatView(){
    ChatView(
        navigator = null,
        doToo = DoTooWithProfiles(
            project = Project(
                id = "74D46CEC-04C8-4E7E-BA2E-B9C7E8D2E958",
                name = "Test is the name",
                description = "Android is my game. Because test is my name",
                ownerId = "",
                collaboratorIds = listOf(
                    "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    "NuZXwLl3a8O3mXRcXFsJzHQgB172"
                ),
                viewerIds = listOf()
            ),
            doToo = DoTooItem(
                id = "",
                title = "Wash the dishes Please",
                description = "To see the preview comment code related to viewModel",
                dueDate = 90L,
                createDate = 789L,
                done = true,
                priority = "High",
                updatedBy = "Baljeet create this doToo item."
            ),
            profiles = listOf(
                User(
                    id = "NuZXwLl3a8O3mXRcXFsJzHQgB172",
                    name = "Baljeet Singh",
                    email = "baljeet@gmail.com",
                    joined = 82782L,
                    avatarUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2Fdo2.png?alt=media&token=701d3864-68e3-445c-9c75-66bc06d44d09"
                ),
                User(
                    id = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    name = "Karandeep Kaur",
                    email = "baljeet@gmail.com",
                    joined = 82782L,
                    avatarUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2F2.png?alt=media&token=f814c406-fa71-4fd7-a37a-e51119a5f107&_gl=1*37amd3*_ga*OTgxMTYwNDY4LjE2ODU2NTc1OTc.*_ga_CW55HF8NVT*MTY4NTY3MDMzMi40LjEuMTY4NTY3MDkwMS4wLjAuMA.."
                )
            )
        )
    )
}