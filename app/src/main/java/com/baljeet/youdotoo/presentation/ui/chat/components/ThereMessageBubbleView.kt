package com.baljeet.youdotoo.presentation.ui.chat.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.getInteractions
import com.baljeet.youdotoo.common.getSampleDoTooWithProfiles
import com.baljeet.youdotoo.common.getSampleMessage
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.models.*
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThereMessageBubbleView(
    message: MessageEntity,
    doToo: DoTooWithProfiles?,
    onLongPress: () -> Unit,
    showSenderInfo: Boolean = true
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = onLongPress,
                onClick = {}
            )
            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalAlignment = Alignment.Top
    ) {


        if (showSenderInfo) {
            Box(
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            DoTooLightBlue
                        } else {
                            DotooBlue
                        },
                        shape = RoundedCornerShape(40.dp)
                    )
                    .padding(3.dp)
            ) {
                AsyncImage(
                    model = doToo?.profiles?.getUserProfilePicture(message.senderId),
                    contentDescription = "avatarImage",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clip(shape = RoundedCornerShape(40.dp))

                )
            }

        } else {
            Spacer(
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
            )
        }

        Column(
            modifier = Modifier
                .widthIn(min = 30.dp, max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            if (showSenderInfo) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.Start)
                ) {

                    Text(
                        text = doToo?.profiles?.getUserName(message.senderId) ?: "Unknown",
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                    )

                    Text(
                        text = message.createdAt.toNiceDateTimeFormat(true),
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(top = 4.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .background(
                        color = DoTooLightBlue,
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                            topStart = 0.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .padding(10.dp)
                    .widthIn(min = 30.dp, max = 200.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                message.attachmentUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = "Attachment image",
                        modifier = Modifier
                            .height(180.dp)
                            .clip(shape = RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop,
                    )
                }
                Text(
                    text = message.message,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 14.sp,
                    color = DotooBlue
                )
            }
            if (message.interactions.isNotEmpty()) {
                EmoticonsSmallPreview(
                    interactions = message.interactions.getInteractions(),
                    onViewClicked = onLongPress
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewThereMessageBubble() {
    ThereMessageBubbleView(
        message = getSampleMessage(),
        doToo = getSampleDoTooWithProfiles(),
        onLongPress = { }
    )
}
