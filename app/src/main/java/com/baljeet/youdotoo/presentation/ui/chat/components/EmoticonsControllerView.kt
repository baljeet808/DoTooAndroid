package com.baljeet.youdotoo.presentation.ui.chat.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.baljeet.youdotoo.domain.models.Interaction

/**
 * Updated by Baljeet singh.
 * **/

@SuppressLint("DiscouragedApi")
@Composable
fun EmoticonsControllerView(
    onItemSelected : (emoticonName : String) -> Unit,
    interactions : ArrayList<Interaction>
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
                        .background(shape = RoundedCornerShape(20.dp), color = Color.Transparent)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewEmoticonControllerView(){
    EmoticonsControllerView(onItemSelected = {}, interactions = arrayListOf(
        Interaction(
            userId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            emoticonName = "feeling_good"
        ),
        Interaction(
            userId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            emoticonName = "wow"
        ),
        Interaction(
            userId = "iz8dz6PufNPGbw9DzWUiZyoTHn62",
            emoticonName = "sad_face"
        )
    ))
}