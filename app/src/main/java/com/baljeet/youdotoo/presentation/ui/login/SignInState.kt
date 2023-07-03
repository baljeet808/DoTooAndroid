package com.baljeet.youdotoo.presentation.ui.login

import com.baljeet.youdotoo.data.dto.UserData


/**
 * Updated by Baljeet singh.
 * **/
data class SignInState(
    val userData: UserData? = null,
    val signInError: String? = null
)
