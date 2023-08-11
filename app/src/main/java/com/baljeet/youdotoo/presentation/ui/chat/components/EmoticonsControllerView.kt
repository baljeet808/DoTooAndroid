package com.baljeet.youdotoo.presentation.ui.chat.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getAllEmoticons
import com.baljeet.youdotoo.common.getRandomId
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.domain.models.User
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

/**
 * Updated by Baljeet singh.
 * **/

@SuppressLint("DiscouragedApi")
@Composable
fun EmoticonsControllerView(
    onItemSelected : (emoticonName : String, message : MessageEntity) -> Unit,
    message: MessageEntity,
    profiles : ArrayList<User>
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
    ){
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 50.dp),
            modifier = Modifier.fillMaxWidth().padding(2.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ){
            items(getAllEmoticons()){ emoticon ->
                val drawableId = remember(emoticon) {
                    context.resources.getIdentifier(
                        emoticon,
                        "drawable",
                        context.packageName
                    )
                }
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = "Emoticon" ,
                    modifier = Modifier
                        .height(42.dp)
                        .width(42.dp)
                        .clickable {
                            onItemSelected(emoticon, message)
                        }
                        .background(shape = RoundedCornerShape(20.dp), color = Color.Transparent)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmoticonControllerView(){
    EmoticonsControllerView(
        onItemSelected = { _, _ -> },
        message = MessageEntity(
            id = "",
            message = "Hey there, This is a new message about your doToo. What do you think.",
            senderId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            createdAt = LocalDateTime.now().toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            isUpdate = false,
            attachmentUrl = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2F2.png?alt=media&token=f814c406-fa71-4fd7-a37a-e51119a5f107&_gl=1*37amd3*_ga*OTgxMTYwNDY4LjE2ODU2NTc1OTc.*_ga_CW55HF8NVT*MTY4NTY3MDMzMi40LjEuMTY4NTY3MDkwMS4wLjAuMA..",
            interactions = "iz8dz6PufNPGbw9DzWUiZyoTHn62,feeling_good | NuZXwLl3a8O3mXRcXFsJzHQgB172,wow | NuZXwLl3a8O3mXRcXFsJzHQgB172,sad_face",
            projectId = getRandomId()
        ),
        profiles = arrayListOf(
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
    )
}