package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooLightBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooDarkerGray
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

/**
 * Updated by Baljeet singh on 18th June, 2023
 * **/
@Composable
fun MessageBoxView(
    onClickSend: (String) -> Unit,
    openCollaboratorsScreen : () -> Unit,
    openPersonTagger : () -> Unit,
    openAttachments : () -> Unit,
    openCustomEmojis : () -> Unit,
    showEditText : Boolean
) {
    var message by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSystemInDarkTheme()) {
                    DotooDarkerGray
                } else {
                    DoTooLightBlue
                }
            )
    ) {

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


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = openAttachments
            ) {
                Icon(
                    Icons.Outlined.Image,
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
                onClick = openCustomEmojis
            ) {
                Icon(
                    Icons.Outlined.EmojiEmotions,
                    contentDescription = "Mark done/not done button",
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
                    backgroundColor = DotooBlue,
                    disabledBackgroundColor = Color.Gray
                ),
                enabled = message.isNotBlank()
            ) {
                Text(
                    text = "Send",
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                )
            }
        }

    }


}


@Preview(showBackground = true)
@Composable
fun PreviewMessageBoxView() {
    MessageBoxView(
        onClickSend = {},
        openCollaboratorsScreen = {},
        openAttachments = {},
        openPersonTagger = {},
        openCustomEmojis = {},
        showEditText = true
    )
}