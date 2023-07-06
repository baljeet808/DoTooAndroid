package com.baljeet.youdotoo.data.local.converters

import kotlinx.datetime.*



fun convertEpochSecondsToLocalDateTime(value : Long): LocalDateTime{
    return  Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
}

fun convertLocalDateTimeToEpochSeconds(value : LocalDateTime): Long{
    return value.toInstant(TimeZone.currentSystemDefault()).epochSeconds
}