package com.baljeet.youdotoo.presentation.ui.chat.components

import android.content.res.Configuration
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.EnumProjectColors
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.data.dto.AttachmentDto
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooRed
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Updated by Baljeet singh on 18th June, 2023
 * **/
@Composable
fun MessageBoxView(
    openCollaboratorsScreen: () -> Unit,
    openPersonTagger: () -> Unit,
    openCamera: () -> Unit,
    project: Project?
) {

    SharedPref.init(LocalContext.current)

    val contentResolver = LocalContext.current.contentResolver


    var attachmentsDTOs by remember {
        mutableStateOf<List<AttachmentDto>>(emptyList())
    }

    val viewModel : MessageBoxViewModel = hiltViewModel()

    val attachmentsStates by viewModel.attachmentStates.collectAsState()


    if(attachmentsStates == MessageBoxViewModel.AttachmentStates.Sent){
        attachmentsDTOs = emptyList()
        viewModel.resetState()
    }

    if(attachmentsStates is MessageBoxViewModel.AttachmentStates.CompletedWithErrors){
        val allAttachments = attachmentsDTOs.toCollection(ArrayList())
        allAttachments.removeIf { a -> (attachmentsStates as MessageBoxViewModel.AttachmentStates.CompletedWithErrors).failedAttachments.none { f-> f == a } }
        attachmentsDTOs = allAttachments
    }

    val scope = rememberCoroutineScope()

    var showToast by remember {
        mutableStateOf(false)
    }

    if(showToast){
        Toast.makeText(LocalContext.current,"You can only send 4 photos at a time.", Toast.LENGTH_SHORT).show()
    }


    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = (4)),
        onResult = { uris ->
            if(uris.size + attachmentsDTOs.size  > 4){
                scope.launch {
                    showToast = true
                    delay(1000)
                    showToast = false
                }
            }

            val previousImages  = attachmentsDTOs.toCollection(ArrayList())
            uris.forEach {
                if(previousImages.none { a -> a.uri == it }){
                    if(previousImages.size < 4) {
                        val mime = MimeTypeMap.getSingleton()
                        mime.getExtensionFromMimeType(contentResolver.getType(it))?.let {mimeType ->
                            previousImages.add(
                                AttachmentDto(
                                    uri = it,
                                    mimeType = mimeType
                                )
                            )
                        }

                    }
                }
            }
            attachmentsDTOs = previousImages
        }
    )

    var message by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = getDarkThemeColor()
            )
    ) {


        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            items(attachmentsDTOs) { attachment ->

                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .padding(5.dp)
                        .clip(
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.TopEnd
                ) {

                    AsyncImage(
                        model = attachment.uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .clip(
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                    if(
                        attachmentsStates == MessageBoxViewModel.AttachmentStates.Empty ||
                        attachmentsStates is MessageBoxViewModel.AttachmentStates.CompletedWithErrors
                        ) {
                        Icon(
                            Icons.Default.Cancel,
                            contentDescription = "Remove attachment button",
                            tint = getLightThemeColor(),
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(30.dp)
                                )
                                .clickable(
                                    onClick = {
                                        val allAttachments = attachmentsDTOs.toCollection(ArrayList())

                                        if (allAttachments.any { a -> a.uri == attachment.uri }) {
                                            allAttachments.remove(attachment)
                                        }
                                        attachmentsDTOs = allAttachments
                                    }
                                )
                        )
                    }
                    if(
                        attachmentsStates == MessageBoxViewModel.AttachmentStates.Sending
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .width(30.dp)
                                .padding(end = 5.dp),
                            color = Color(project?.color?:EnumProjectColors.Purple.longValue)
                        )
                    }
                    if(attachmentsStates is MessageBoxViewModel.AttachmentStates.CompletedWithErrors)
                    {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription ="Error icon" ,
                            modifier = Modifier.width(30.dp).padding(end = 5.dp),
                            tint = DoTooRed
                        )
                    }
                }

            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = message,
                onValueChange = {
                    message = it
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 16.sp,
                    color = getTextColor()
                ),
                placeholder = {
                    Text(
                        text = "Write message here...",
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (message.isNotBlank() || attachmentsDTOs.isNotEmpty()) {

                            project?.let {
                                viewModel.sendMessage(
                                    messageString = message,
                                    isUpdate = false,
                                    updateMessage = "",
                                    attachments = attachmentsDTOs,
                                    project = project
                                )
                            }
                            message = ""
                        }
                    }
                ),
                maxLines = 5,
                modifier = Modifier
                    .weight(1f)
                    .padding(5.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            Box(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            if (message.isNotBlank() || attachmentsDTOs.isNotEmpty()) {
                                project?.let {
                                    viewModel.sendMessage(
                                        messageString = message,
                                        isUpdate = false,
                                        updateMessage = "",
                                        attachments = attachmentsDTOs,
                                        project = project
                                    )
                                }
                                message = ""
                            }
                        }
                    )
                    .background(
                        color = if (message.isNotBlank() || attachmentsDTOs.isNotEmpty()) {
                            Color(project?.color ?: EnumProjectColors.Purple.longValue)
                        } else {
                            Color.Gray
                        },
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send message button",
                    tint = Color.White,
                    modifier = Modifier
                        .width(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
        }


        /**
         * Bottom row of all buttons
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    if(attachmentsDTOs.size <4) {
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
                }
            ) {
                Icon(
                    Icons.Outlined.PhotoLibrary,
                    contentDescription = "Attachments Button",
                    tint = Color.Gray
                )
            }

            IconButton(
                onClick = openPersonTagger
            ) {
                Icon(
                    Icons.Default.AlternateEmail,
                    contentDescription = "Mention button",
                    tint = Color.Gray
                )
            }

            IconButton(
                onClick = openCollaboratorsScreen
            ) {
                Icon(
                    Icons.Outlined.PersonAdd,
                    contentDescription = "Add person button",
                    tint = Color.Gray
                )
            }

            IconButton(
                onClick = openCamera
            ) {
                Icon(
                    Icons.Outlined.CameraAlt,
                    contentDescription = "Open Camera button",
                    tint = Color.Gray
                )
            }
        }

    }


}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMessageBoxView() {
    MessageBoxView(
        openCollaboratorsScreen = {},
        openPersonTagger = {},
        openCamera = {},
        project = getSampleProject()
    )
}