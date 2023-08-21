package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var id : String,
    var name : String,
    var email :String,
    var joined : Long,
    var avatarUrl: String = "",
    var firebaseToken : String = ""
):Parcelable
