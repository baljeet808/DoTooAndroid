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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getInteractions
import com.baljeet.youdotoo.common.getSampleMessage
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.toNiceDateTimeFormat
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.*
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooLightBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue

/**
 * Updated by Baljeet singh on 18th June, 2023 at 1:05 PM.
 * **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SenderMessageBubbleView(
    message: MessageEntity,
    users : List<UserEntity>,
    onLongPress: () -> Unit,
    showSenderInfo: Boolean = true
) {


    SharedPref.init(LocalContext.current)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onLongClick = onLongPress,
                onClick = {}
            )
            .padding(start = 10.dp, end = 10.dp, top = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .widthIn(min = 30.dp, max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.End
        ) {
            if(showSenderInfo) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.End)
                ) {
                    Text(
                        text = message.createdAt.toNiceDateTimeFormat(true),
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .padding(top = 4.dp)
                    )
                    Text(
                        text = users.getUserName(message.senderId),
                        fontFamily = FontFamily(Nunito.Bold.font),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                    )
                }
            }
            Column(modifier = Modifier
                .background(
                    color = DotooBlue,
                    shape = RoundedCornerShape(
                        topEnd = 0.dp,
                        topStart = 20.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    )
                )
                .padding(10.dp)
                .widthIn(min = 30.dp, max = 200.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                message.attachmentUrl?.let {url ->
                    AsyncImage(
                        model = url,
                        contentDescription ="Attachment image",
                        modifier = Modifier
                            .height(180.dp)
                            .clip(shape = RoundedCornerShape(20.dp))
                        ,
                        contentScale = ContentScale.Crop,
                    )
                }
                Text(
                    text = message.message,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 14.sp,
                    color = DoTooLightBlue
                )
            }
            if( message.interactions.isNotEmpty()){
                EmoticonsSmallPreview(
                    interactions = message.interactions.getInteractions(),
                    onViewClicked = onLongPress
                )
            }
        }

        if(showSenderInfo) {
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
            ){
                AsyncImage(
                    model = users.getUserProfilePicture(message.senderId),
                    contentDescription = "avatarImage",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                        .clip(shape = RoundedCornerShape(40.dp))
                )
            }
        }else{
            Spacer(modifier = Modifier.width(30.dp).height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSenderMessageBubble() {
    SenderMessageBubbleView(
        message = getSampleMessage(),
        users = listOf(
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile()
        ).map { it.toUserEntity() },
        onLongPress = { }
    )
}

fun List<UserEntity>.getUserProfilePicture(userId: String): String {
    if(userId == SharedPref.userId){
        return SharedPref.userAvatar
    }
    return this.first { user -> user.id == userId }.avatarUrl
}

fun List<UserEntity>.getUserName(userId: String): String {
    if(userId == SharedPref.userId){
        return SharedPref.userName
    }
    return this.first { user -> user.id == userId }.name
}