package com.baljeet.youdotoo.domain.models

import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity

data class UserInvitation(
    var invitationEntity: InvitationEntity? = null,
    var user: UserEntity? = null
)
