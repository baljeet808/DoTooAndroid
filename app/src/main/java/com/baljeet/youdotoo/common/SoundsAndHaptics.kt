package com.baljeet.youdotoo.common

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.baljeet.youdotoo.R

/**
 * Updated by Baljeet singh on 17th June,2023 at 12:21 AM.
 * **/

fun playBoopSound(context: Context) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    audioManager.playSoundEffect(
        if (true) {
            AudioManager.ADJUST_UNMUTE
        } else {
            AudioManager.ADJUST_UNMUTE
        },
        1.0f
    )
}

fun addHapticFeedback(hapticFeedback : HapticFeedback){
    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
}

fun playWooshSound(context : Context){
    val mMediaPlayer = MediaPlayer.create(context, R.raw.woosh)
    mMediaPlayer.start()
}