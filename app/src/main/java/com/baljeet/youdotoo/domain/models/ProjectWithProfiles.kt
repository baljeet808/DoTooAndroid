package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectWithProfiles(
    var project : Project,
    var profiles : List<User>?
):Parcelable
