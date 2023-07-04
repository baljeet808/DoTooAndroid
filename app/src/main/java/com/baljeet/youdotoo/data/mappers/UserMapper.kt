package com.baljeet.youdotoo.data.mappers

import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.models.User


fun User.toUserEntity() : UserEntity{
    return UserEntity(
        id = id,
        name = name,
        email = email,
        joined = joined,
        avatarUrl = avatarUrl
    )
}


fun UserEntity.toUser(): User{
    return User(
        id = id,
        name = name,
        email = email,
        joined = joined,
        avatarUrl = avatarUrl
    )
}