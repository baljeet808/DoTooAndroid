package com.baljeet.youdotoo.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoTooItem(
    val id : String,
    val title : String,
    val description : String,
    val dueDate : Long,
    val createDate : Long,
    var done : Boolean,
    val priority : String,
    var updatedBy : String
):Parcelable
