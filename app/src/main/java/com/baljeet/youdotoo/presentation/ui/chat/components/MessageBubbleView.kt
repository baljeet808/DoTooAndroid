package com.baljeet.youdotoo.ui.chat

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.models.*
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getOnCardColor
import com.baljeet.youdotoo.util.toNiceDateTimeFormat
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

/**
 * Updated by Baljeet singh on 18th June, 2023 at 1:05 PM.
 * **/
@Composable
fun MessageBubbleView(
    message: Message,
    doToo : DoTooWithProfiles?,
    onLongPress : () -> Unit,
    userId : String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = if(userId == message.senderId){
            Alignment.End
        }else{
            Alignment.Start
        }
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =  if(message.senderId == userId){
                Arrangement.spacedBy(10.dp,Alignment.End)
            }else{
                Arrangement.spacedBy(10.dp, Alignment.Start)
            },
            verticalAlignment = Alignment.Bottom,
        ) {
            if(message.senderId != userId){
                AsyncImage(
                    model = doToo?.profiles?.getUserProfilePicture(message.senderId),
                    contentDescription = "avatarImage",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(color = getOnCardColor(), shape = RoundedCornerShape(40.dp))
                        .clip(shape = RoundedCornerShape(40.dp))
                )
            }
            Column(modifier = Modifier
                .background(
                    color = getOnCardColor(),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(10.dp)
                .fillMaxWidth(fraction = .65f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                message.attachmentUrl?.let {url ->
                    AsyncImage(
                        model = url,
                        contentDescription ="Attachment image",
                        modifier = Modifier
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = message.message,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 13.sp
                )
            }
            if(message.senderId == userId){
                AsyncImage(
                    model = doToo?.profiles?.getUserProfilePicture(message.senderId),
                    contentDescription = "avatarImage",
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .background(color = getOnCardColor(), shape = RoundedCornerShape(40.dp))
                        .clip(shape = RoundedCornerShape(40.dp))
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =  if(message.senderId == userId){
                Arrangement.spacedBy(10.dp,Alignment.End)
            }else{
                Arrangement.spacedBy(10.dp, Alignment.Start)
            },
            verticalAlignment = Alignment.Bottom,
        ) {

            if(message.senderId == userId) {
                EmoticonsSmallPreview(
                    interactions = message.interactions.getInteractions(),
                    onViewClicked = onLongPress
                )
            }
            Text(
                text = message.createdAt .toNiceDateTimeFormat(),
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = if(userId == message.senderId){
                    TextAlign.End
                }else{
                    TextAlign.Start
                },
                modifier = Modifier.padding(start = 60.dp, end = 60.dp)
            )
            if(message.senderId != userId){
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
fun PreviewMessageBubble(){
    MessageBubbleView(
        message = Message(
            id = "",
            message = "Hey there, This is a new message about your doToo. What do you think.",
            senderId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            createdAt = LocalDateTime.now().toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            isUpdate = false,
            attachmentUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2F2.png?alt=media&token=f814c406-fa71-4fd7-a37a-e51119a5f107&_gl=1*37amd3*_ga*OTgxMTYwNDY4LjE2ODU2NTc1OTc.*_ga_CW55HF8NVT*MTY4NTY3MDMzMi40LjEuMTY4NTY3MDkwMS4wLjAuMA..",
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
                viewerIds = listOf()
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
        onLongPress = {  },
        userId = "NuZXwLl3a8O3mXRcXFsJzHQgB172"
    )
}

fun List<User>.getUserProfilePicture(userId : String):String{
    return this.first { user -> user.id == userId }.avatarUrl
}