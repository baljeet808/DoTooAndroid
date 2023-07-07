package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectWithProfiles(
    var project : Project,
    var profiles : List<User>?
):Parcelable

@Parcelize
data class ProjectWithEveryThing(
    var project : Project,
    var doToos : List<DoTooItem>,
    var profiles : List<User>?
):Parcelable
