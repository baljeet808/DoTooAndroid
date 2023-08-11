@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.ChatScreenBottomSheetTypes
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.presentation.ui.chat.components.ChatViewMainContent
import com.baljeet.youdotoo.presentation.ui.chat.components.EmoticonsControllerView
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatView(
    participants : List<UserEntity>,
    messages: List<MessageEntity>,
    sendMessage: (message: String) -> Unit,
    showAttachments: (messages: ArrayList<MessageEntity>) -> Unit,
    interactOnMessage : (message : MessageEntity, emoticon : String) -> Unit
) {

    SharedPref.init(LocalContext.current)

    var selectedMessage : MessageEntity? by remember {
        mutableStateOf(null)
    }

    var currentBottomSheet: ChatScreenBottomSheetTypes? by remember {
        mutableStateOf(null)
    }
    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )

    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.hide()
        }
    }
    val openSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.expand()
        }
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            currentBottomSheet?.let {
                when (it) {
                    ChatScreenBottomSheetTypes.MESSAGE_EMOTICONS -> {
                        selectedMessage?.let {selectedMessage->
                            EmoticonsControllerView(
                                message = selectedMessage,
                                onItemSelected = { emoticon, msg ->
                                    interactOnMessage( msg,emoticon )
                                    closeSheet()
                                },
                                profiles = arrayListOf(getSampleProfile())
                            )
                        }
                    }
                    ChatScreenBottomSheetTypes.CUSTOM_EMOTICONS -> TODO()
                    ChatScreenBottomSheetTypes.PERSON_TAGGER -> TODO()
                    ChatScreenBottomSheetTypes.COLLABORATOR_SCREEN -> TODO()
                }
            }
        }
    ) {
        ChatViewMainContent(
            messages = messages,
            sendMessage = sendMessage,
            openEmoticons = { message ->
                selectedMessage = message
                currentBottomSheet = ChatScreenBottomSheetTypes.MESSAGE_EMOTICONS
                openSheet()
            },
            showAttachments = showAttachments,
            openCollaboratorsScreen = {
                currentBottomSheet = ChatScreenBottomSheetTypes.COLLABORATOR_SCREEN
                openSheet()
            },
            openPersonTagger = {
                currentBottomSheet = ChatScreenBottomSheetTypes.PERSON_TAGGER
                openSheet()
            },
            openAttachments = {

            },
            openCustomEmoticons = {
                currentBottomSheet = ChatScreenBottomSheetTypes.CUSTOM_EMOTICONS
                openSheet()
            },
            participants = participants
        )
    }


}



