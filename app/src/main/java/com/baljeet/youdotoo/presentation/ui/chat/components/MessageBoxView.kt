package com.baljeet.youdotoo.presentation.ui.chat.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

/**
 * Updated by Baljeet singh on 18th June, 2023
 * **/
@Composable
fun MessageBoxView(
    onClickSend: (String) -> Unit,
    openCollaboratorsScreen : () -> Unit,
    openPersonTagger : () -> Unit,
    pickAttachments : () -> Unit,
    openCamera : () -> Unit,
    showEditText : Boolean,
    attachments : ArrayList<Uri>,
    removeAttachment : (uri : Uri) -> Unit
) {

    SharedPref.init(LocalContext.current)

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
        ){

            items(attachments){attachment ->

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
                        model = attachment,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .clip(
                                shape = RoundedCornerShape(10.dp)
                            )
                    )

                    Icon(
                        Icons.Default.Cancel,
                        contentDescription ="Remove attachment button",
                        tint = getLightThemeColor(),
                        modifier = Modifier
                            .clip(
                                RoundedCornerShape(30.dp)
                            )
                            .clickable(
                                onClick = {
                                    removeAttachment(attachment)
                                }
                            )
                    )
                }

            }

        }



        if(showEditText) {
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
                        onClickSend(message)
                        message = ""
                    }
                ),
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
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
                onClick = pickAttachments
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

            Button(
                onClick = {
                    onClickSend(message)
                    message = ""
                },
                modifier = Modifier,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = getLightThemeColor(),
                    disabledBackgroundColor = Color.Gray
                ),
                enabled = message.isNotBlank()
            ) {
                Text(
                    text = "Send",
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = getTextColor()
                )
            }
        }

    }


}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMessageBoxView() {
    MessageBoxView(
        onClickSend = {},
        openCollaboratorsScreen = {},
        pickAttachments = {},
        openPersonTagger = {},
        openCamera = {},
        showEditText = true,
        attachments = arrayListOf(),
        removeAttachment = {}
    )
}