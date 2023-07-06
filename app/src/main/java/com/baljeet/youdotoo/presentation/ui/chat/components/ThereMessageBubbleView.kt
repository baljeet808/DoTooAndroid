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
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.domain.models.*
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

/**
 * Updated by Baljeet singh on 18th June, 2023 at 1:05 PM.
 * **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ThereMessageBubbleView(
    message: Message,
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
        message = Message(
            id = "",
            message = "Hey there, This is a new message about your doToo. What do you think.",
            senderId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            createdAt = LocalDateTime.now().toKotlinLocalDateTime()
                .toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            isUpdate = false,
            attachmentUrl = null,
            interactions = arrayListOf(
                "iz8dz6PufNPGbw9DzWUiZyoTHn62,feeling_good",
                "NuZXwLl3a8O3mXRcXFsJzHQgB172,wow",
                "NuZXwLl3a8O3mXRcXFsJzHQgB172,sad_face"
            )
        ),
        doToo = DoTooWithProfiles(
            project = Project(
                id = "74D46CEC-04C8-4E7E-BA2E-B9C7E8D2E958",
                name = "Test is the name",
                description = "Android is my game. Because test is my name",
                ownerId = "",
                collaboratorIds = listOf(
                    "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    "NuZXwLl3a8O3mXRcXFsJzHQgB172"
                ),
                viewerIds = listOf(),
                update = ""
            ),
            doToo = DoTooItem(
                id = "",
                title = "",
                description = "",
                dueDate = 90L,
                createDate = 789L,
                done = false,
                priority = "High",
                updatedBy = "Baljeet create this doToo item."
            ),
            profiles = listOf(
                User(
                    id = "NuZXwLl3a8O3mXRcXFsJzHQgB172",
                    name = "Baljeet Singh",
                    email = "baljeet@gmail.com",
                    joined = 82782L,
                    avatarUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2Fdo2.png?alt=media&token=701d3864-68e3-445c-9c75-66bc06d44d09"
                ),
                User(
                    id = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
                    name = "Karandeep Kaur",
                    email = "baljeet@gmail.com",
                    joined = 82782L,
                    avatarUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2F2.png?alt=media&token=f814c406-fa71-4fd7-a37a-e51119a5f107&_gl=1*37amd3*_ga*OTgxMTYwNDY4LjE2ODU2NTc1OTc.*_ga_CW55HF8NVT*MTY4NTY3MDMzMi40LjEuMTY4NTY3MDkwMS4wLjAuMA.."
                )
            )
        ),
        onLongPress = { }
    )
}
