package com.baljeet.youdotoo.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Project(
    var id : String,
    var name : String,
    var description : String,
    var ownerId : String,
    var collaboratorIds : List<String>,
    var viewerIds: List<String>,
    var update : String,
    var color : String,
    var updatedAt : Long
): Parcelable