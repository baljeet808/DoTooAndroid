package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.compose.foundation.background
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
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor

@Composable
fun ChatViewMainContent(
    participants : List<UserEntity>,
    messages: List<MessageEntity>,
    sendMessage: (messageString: String) -> Unit,
    openEmoticons: (message: MessageEntity) -> Unit,
    openCustomEmoticons: () -> Unit,
    openCollaboratorsScreen: () -> Unit,
    openPersonTagger: () -> Unit,
    openAttachments: () -> Unit,
    showAttachments: (messages: ArrayList<MessageEntity>) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getDarkThemeColor()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        /**
         *LazyColumn of Chat
         * **/
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 5.dp, top = 5.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
            reverseLayout = true
        ) {
            items(messages, key = {it.id}) { message ->
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
                        onLongPress = {
                            openEmoticons(message)
                        },
                        users= participants ,
                        showSenderInfo  = showUserInfo
                    )
                }else{
                    ThereMessageBubbleView(
                        message = message,
                        users = participants,
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
        openEmoticons = {},
        openCustomEmoticons = {},
        openCollaboratorsScreen = {},
        openAttachments = {},
        showAttachments = {},
        openPersonTagger = {},
        participants = listOf(
            getSampleProfile(),
            getSampleProfile(),
        ).map { it.toUserEntity() }
    )
}