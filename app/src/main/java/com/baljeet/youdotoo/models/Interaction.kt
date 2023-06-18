package com.baljeet.youdotoo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Updated by Baljeet singh on 18th June, 2023 at 4:00PM.
 * **/
@Parcelize
data class Interaction(
    val userId : String,
    val emoticonName : String
):Parcelable
