package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DoTooWithProfiles(
    var project : Project,
    var doToo : DoTooItem,
    var profiles : List<User>?
):Parcelable
