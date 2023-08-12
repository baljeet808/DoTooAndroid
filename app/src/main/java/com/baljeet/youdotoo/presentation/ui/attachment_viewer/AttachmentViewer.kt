package com.baljeet.youdotoo.presentation.ui.attachment_viewer

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getSampleMessage
import com.baljeet.youdotoo.data.local.entities.MessageEntity
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor

@Composable
fun AttachmentViewer(
    message : MessageEntity?,
    goBack : () -> Unit,
    downloadImage : () -> Unit
) {

    SharedPref.init(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = getLightThemeColor()
            )
            .padding(
                10.dp
            )
    ) {

        message?.attachmentUrl?.let {
            AsyncImage(
                model = Uri.parse(it),
                contentDescription = "Attachment preview",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAttachmentViewer(){
    AttachmentViewer(
        message = getSampleMessage(),
        goBack = {},
        downloadImage = {}
    )
}