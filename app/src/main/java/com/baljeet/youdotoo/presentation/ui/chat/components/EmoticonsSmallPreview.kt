package com.baljeet.youdotoo.presentation.ui.chat.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.Interaction
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getTextColor

/**
 * Updated by Baljeet singh on 18th June,2023 at 3:30 PM.
 * **/
@SuppressLint("DiscouragedApi")
@Composable
fun EmoticonsSmallPreview(
    interactions : ArrayList<Interaction>,
    onViewClicked : () -> Unit
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .clickable(onClick = onViewClicked),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = interactions.size.toString(),
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 11.sp,
            color = getTextColor()
        )
        Spacer(modifier = Modifier.width(2.dp))
        interactions.take(3).forEach {interaction->
            val drawableId = remember(interaction.emoticonName) {
                context.resources.getIdentifier(
                    interaction.emoticonName,
                    "drawable",
                    context.packageName
                )
            }
            Image(
                painter = painterResource(id = drawableId),
                contentDescription = "Emoticon" ,
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp)
                    .background(shape = RoundedCornerShape(20.dp), color = Color.Transparent)
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewEmoticonsSmallPreview(){
    EmoticonsSmallPreview(
        interactions = arrayListOf(
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
        )
    ) {

    }
}