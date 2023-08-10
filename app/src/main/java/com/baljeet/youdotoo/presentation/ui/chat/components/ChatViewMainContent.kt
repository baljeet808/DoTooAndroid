package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleDoTooWithProfiles
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.domain.models.Message
import com.baljeet.youdotoo.presentation.ui.theme.DoTooLightBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooDarkerGray
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor

@Composable
fun ChatViewMainContent(
    messages: List<Message>,
    sendMessage: (messageString: String) -> Unit,
    toggleIsDone: () -> Unit,
    openEmoticons: (message: Message) -> Unit,
    openCustomEmoticons: () -> Unit,
    openCollaboratorsScreen: () -> Unit,
    openPersonTagger: () -> Unit,
    openAttachments: () -> Unit,
    showAttachments: (messages: ArrayList<Message>) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getDarkThemeColor()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        /**
         *Column of description and toolbox for editing this Dotoo
         * **/
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = tween(durationMillis = 200))
                .background(
                    color = if (isSystemInDarkTheme()) {
                        DotooDarkerGray
                    } else {
                        DoTooLightBlue
                    }
                )
        ) {
         /*   *//**
             *Top row about Dotoo name and check box
             * **//*
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp),
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
                        toggleIsDone()
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
            if (lazyListState.isScrolled.not()) {
                Row(
                    modifier = Modifier
                        .shadow(elevation = 0.dp, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {


                    IconButton(
                        onClick = { *//*TODO*//* },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Default.NotificationsOff,
                            contentDescription = "Turn off notification for this doToo",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { *//*TODO*//* },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Default.DeleteForever,
                            contentDescription = "Delete this doToo",
                            tint = Color.Gray
                        )
                    }

                    IconButton(
                        onClick = { *//*TODO*//* },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Information about this doToo",
                            tint = Color.Gray
                        )
                    }

                    doToo.profiles?.let { profiles ->
                        profilesLazyRow(profiles = profiles, onTapProfiles = {})
                    } ?: kotlin.run {
                        Spacer(modifier = Modifier.weight(.5f))
                        IconButton(
                            onClick = { *//*TODO*//* },
                            modifier = Modifier
                        ) {
                            Icon(
                                Icons.Outlined.PersonAdd,
                                contentDescription = "Add collaborator button",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }*/
        }
        /**
         *LazyColumn of Chat
         * **/
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8F)
                .padding(bottom = 5.dp, top = 5.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
            reverseLayout = true
        ) {
            items(messages) { message ->
                val itemIndex = messages.indexOf(message)
                val nextMessage =
                    if (messages.last() == message) {
                        null
                    } else {
                        messages[itemIndex + 1]
                    }
                val isThisFromMe = message.senderId == SharedPref.userId
                val showUserInfo = nextMessage?.senderId != message.senderId

                if(isThisFromMe){
                    SenderMessageBubbleView(
                        message = message,
                        doToo = getSampleDoTooWithProfiles(),
                        onLongPress = {
                            openEmoticons(message)
                        },
                        showSenderInfo  = showUserInfo
                    )
                }else{
                    ThereMessageBubbleView(
                        message = message,
                        doToo = getSampleDoTooWithProfiles(),
                        onLongPress = {
                            openEmoticons(message)
                        },
                        showSenderInfo  = showUserInfo
                    )
                }
            }
        }
        /**
         *SendBox
         * **/
        MessageBoxView(
            onClickSend = { message ->
                sendMessage(message)
            },
            showEditText =  lazyListState.isScrolled.not(),
            openAttachments = openAttachments,
            openCollaboratorsScreen = openCollaboratorsScreen,
            openPersonTagger = openPersonTagger,
            openCustomEmojis = openCustomEmoticons
        )

    }
}


@Preview(showBackground = true)
@Composable
fun PreviewChatView() {
    ChatViewMainContent(
        messages = listOf(),
        sendMessage = {},
        toggleIsDone = {},
        openEmoticons = {},
        openCustomEmoticons = {},
        openCollaboratorsScreen = {},
        openAttachments = {},
        showAttachments = {},
        openPersonTagger = {}
    )
}