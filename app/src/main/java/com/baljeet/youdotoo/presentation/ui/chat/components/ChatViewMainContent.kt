package com.baljeet.youdotoo.presentation.ui.chat.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatViewMainContent(
    participants : List<UserEntity>,
    messages: LazyPagingItems<MessageEntity>,
    project : Project?,
    sendMessage: (messageString: String, attachments : List<Uri>) -> Unit,
    openEmoticons: (message: MessageEntity) -> Unit,
    openCollaboratorsScreen: () -> Unit,
    openPersonTagger: () -> Unit,
    showAttachment: (messages: MessageEntity) -> Unit,
    modifier: Modifier,
    onClose : () -> Unit
) {

    SharedPref.init(LocalContext.current)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            ),
        contentAlignment = Alignment.TopCenter
    ) {


        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            /**
             *LazyColumn of Chat
             * **/
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 5.dp, top = 5.dp)
                ,
                verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.Bottom),
                reverseLayout = true
            ) {

                if(participants.isNotEmpty()) {
                    items(
                        count = messages.itemCount,
                        key = messages.itemKey { message -> message.id },
                        contentType = messages.itemContentType { "Messages" }
                    ) { index: Int ->

                        val message: MessageEntity? = messages[index]

                        message?.let {

                            val nextMessage = if (messages[messages.itemCount - 1] == message) {
                                null
                            } else {
                                messages[index + 1]
                            }

                            val isThisFromMe = message.senderId == SharedPref.userId
                            val showUserInfo = nextMessage?.senderId != message.senderId

                            if (isThisFromMe) {
                                SenderMessageBubbleView(
                                    message = message,
                                    onLongPress = {
                                        openEmoticons(message)
                                    },
                                    users = participants,
                                    showSenderInfo = showUserInfo,
                                    showAttachment = {
                                        showAttachment(message)
                                    }
                                )
                            } else {
                                ThereMessageBubbleView(
                                    message = message,
                                    users = participants,
                                    onLongPress = {
                                        openEmoticons(message)
                                    },
                                    showSenderInfo = showUserInfo
                                )
                            }
                        }

                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
            /**
             *SendBox
             * **/
            MessageBoxView(
                onClickSend = { message ->
                    sendMessage(message, selectedImageUris)
                },
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
                },
                project = project
            )

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ){



            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.9f)
                    .blur(
                        radius = 20.dp
                    )
                    .background(
                        color = Color(project?.color ?: EnumProjectColors.Purple.longValue)
                    )
            ){

            }
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                /**
                 * Close icon button
                 * **/
                /**
                 * Close icon button
                 * **/
                /**
                 * Close icon button
                 * **/

                /**
                 * Close icon button
                 * **/
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = getLightThemeColor(),
                            shape = RoundedCornerShape(40.dp)
                        )

                ) {
                    Icon(
                        Icons.Default.ArrowBackIos,
                        contentDescription = "Button to close current screen.",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                /**
                 * Title
                 * **/
                Text(
                    text = project?.name?:"",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

    }

}


