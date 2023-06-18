package com.baljeet.youdotoo.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.theme.getCardColor
import com.baljeet.youdotoo.ui.theme.getOppositeOnCardColor
import com.baljeet.youdotoo.ui.theme.getTextColor

/**
 * Updated by Baljeet singh on 18th June, 2023
 * **/
@Composable
fun MessageBoxView(
    onClickSend: (String) -> Unit,
    onClickAttachment: () -> Unit
) {
    var message by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = getCardColor(),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onClickAttachment,
                modifier = Modifier.weight(.1F)
            ) {
                Icon(
                    Icons.Default.Attachment,
                    contentDescription = "Attachments Button",
                    tint = getOppositeOnCardColor()
                )
            }

            TextField(
                value = message,
                onValueChange = {
                    message = it
                },
                textStyle = TextStyle(
                    fontFamily = FontFamily(Nunito.Normal.font),
                    fontSize = 14.sp
                ),
                placeholder = {
                    Text(
                        text = "Write message here...",
                        fontFamily = FontFamily(Nunito.Normal.font),
                        fontSize = 14.sp
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        onClickSend(message)
                    }
                ),
                maxLines = 5,
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(20.dp),
                        brush = Brush.linearGradient(
                            colors = listOf(
                                getTextColor(),
                                getTextColor()
                            )
                        )
                    )
                    .weight(0.8F),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp)
            )


            IconButton(
                onClick = { onClickSend(message) },
                modifier = Modifier
                    .weight(.1F)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Send Button",
                    tint = getOppositeOnCardColor()
                )

            }
        }

    }


}


@Preview(showBackground = true)
@Composable
fun PreviewMessageBoxView() {
    MessageBoxView(
        onClickSend = {}) {

    }
}