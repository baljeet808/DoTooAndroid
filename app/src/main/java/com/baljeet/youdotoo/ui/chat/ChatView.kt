package com.baljeet.youdotoo.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.ui.theme.getCardColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

/**
 * Updated by Baljeet singh on 18th June, 2023 at 10:00 AM.
 * **/
@Destination
@Composable
fun ChatView(
    navigator: DestinationsNavigator?,
    doToo : DoTooWithProfiles
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = getCardColor())
    ) {

    }
}