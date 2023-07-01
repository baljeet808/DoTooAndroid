@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.baljeet.youdotoo.presentation.ui.chat

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.BottomSheetType
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.domain.models.Message
import com.baljeet.youdotoo.presentation.ui.chat.components.ChatViewMainContent
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh on 18th June, 2023 at 10:00 AM.
 * **/
@Composable
fun ChatView(
    doToo: DoTooWithProfiles,
    messages: List<Message>,
    sendMessage: (message: String) -> Unit,
    toggleIsDone: () -> Unit,
    showAttachments: (messages: ArrayList<Message>) -> Unit
) {


    var currentBottomSheet: BottomSheetType? by remember {
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
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp)
            ),
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            currentBottomSheet?.let {
                when (it) {
                    BottomSheetType.TYPE1 -> {

                    }
                    BottomSheetType.TYPE2 -> {

                    }
                }
            }
        }
    ) {
        ChatViewMainContent(
            doToo = doToo,
            messages = messages,
            sendMessage = sendMessage,
            toggleIsDone = toggleIsDone,
            openEmoticons = {

            },
            showAttachments = showAttachments,
            openCollaboratorsScreen = {

            },
            openPersonTagger = {

            },
            openAttachments = {

            },
            openCustomEmoticons = {

            }
        )
    }


}



