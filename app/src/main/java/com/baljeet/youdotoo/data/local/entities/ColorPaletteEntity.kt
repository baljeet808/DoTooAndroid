package com.baljeet.youdotoo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "palettes")
data class ColorPaletteEntity(
    @PrimaryKey
    var id : String,
    var paletteName : String,
    var nightDark : Long,
    var nightLight : Long,
    var dayDark: Long,
    var dayLight: Long
)