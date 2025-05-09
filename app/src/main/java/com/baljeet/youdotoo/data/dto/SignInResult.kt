package com.baljeet.youdotoo.data.dto

/**
 * Updated by Baljeet singh.
 * **/
data class SignInResult(
    val data : UserData?,
    val errorMessage : String?
)

data class UserData(
    val userId : String,
    val userName : String?,
    val userEmail : String?,
    val profilePictureUrl : String?
)