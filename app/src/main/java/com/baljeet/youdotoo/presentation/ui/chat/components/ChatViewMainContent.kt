package com.baljeet.youdotoo.presentation.ui.chat.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.isScrolled
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatViewMainContent(
    participants : List<UserEntity>,
    messages: List<MessageEntity>,
    sendMessage: (messageString: String) -> Unit,
    openEmoticons: (message: MessageEntity) -> Unit,
    openCollaboratorsScreen: () -> Unit,
    openPersonTagger: () -> Unit,
    showAttachments: (messages: ArrayList<MessageEntity>) -> Unit,
    modifier: Modifier
) {
    val lazyListState = rememberLazyListState()

    var selectedImageUris by remember {
        mutableStateOf<List<Uri>>(emptyList())
    }

    val scope = rememberCoroutineScope()

    var showToast by remember {
        mutableStateOf(false)
    }


    if(showToast){
        Toast.makeText(LocalContext.current,"You can only send 4 photos at a time.",Toast.LENGTH_SHORT).show()
    }

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = (4)),
        onResult = { uris ->
            if(uris.size + selectedImageUris.size  > 4){
                scope.launch {
                    showToast = true
                    delay(1000)
                    showToast = false
                }
            }

            val previousImages  = selectedImageUris.toCollection(ArrayList())
            uris.forEach {
                if(previousImages.none { uri -> uri == it }){
                    if(previousImages.size < 4) {
                        previousImages.add(it)
                    }
                }
            }
            selectedImageUris = previousImages
        }
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = getLightThemeColor()),
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
            pickAttachments = {
                if(selectedImageUris.size <4) {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }else{
                    scope.launch{
                        showToast = true
                        delay(1000)
                        showToast = false
                    }
                }
            },
            openCollaboratorsScreen = openCollaboratorsScreen,
            openPersonTagger = openPersonTagger,
            openCamera = {

            },
            attachments  = selectedImageUris.toCollection(ArrayList()),
            removeAttachment = { attachment ->
                val selectedImages = selectedImageUris.toCollection(ArrayList())
                if(selectedImages.any { uri -> uri == attachment }){
                    selectedImages.remove(attachment)
                }
                selectedImageUris = selectedImages
            }
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
        openCollaboratorsScreen = {},
        showAttachments = {},
        openPersonTagger = {},
        participants = listOf(
            getSampleProfile(),
            getSampleProfile(),
        ).map { it.toUserEntity() },
        modifier = Modifier
    )
}
