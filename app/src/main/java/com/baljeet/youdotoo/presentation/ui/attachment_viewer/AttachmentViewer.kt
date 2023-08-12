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
import com.baljeet.youdotoo.common.getRandomAvatar
import com.baljeet.youdotoo.presentation.ui.theme.getLightThemeColor

@Composable
fun AttachmentViewer(
    attachmentUrl : String?,
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

        AsyncImage(
            model = Uri.parse(attachmentUrl),
            contentDescription = "Attachment preview",
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAttachmentViewer(){
    AttachmentViewer(
        attachmentUrl = getRandomAvatar(),
        goBack = {},
        downloadImage = {}
    )
}