package com.baljeet.youdotoo.presentation.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.ConstSampleAvatarUrl
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.dto.UserData
import com.baljeet.youdotoo.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.Clock
import java.time.LocalDateTime
import java.util.TimeZone
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {


    var state = mutableStateOf(SignInState())
        private set

    fun updateUser (userData: UserData){
        val currentTime = LocalDateTime.now().toKotlinLocalDateTime().toInstant(kotlinx.datetime.TimeZone.currentSystemDefault()).epochSeconds
        val newUser = User(
            id = userData.userId,
            name = userData.userName?:"Unknown",
            email = userData.userEmail?:"Not given",
            joined = currentTime,
            avatarUrl = userData.profilePictureUrl ?: ConstSampleAvatarUrl
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userData.userId)
            .set(newUser).addOnSuccessListener {
                SharedPref.isUserLoggedIn = true
                SharedPref.userId = userData.userId
                SharedPref.userName =userData.userName?:"Unknown"
                SharedPref.userJoined = currentTime
                SharedPref.userAvatar = userData.profilePictureUrl ?: ConstSampleAvatarUrl
                SharedPref.userEmail = userData.userEmail?:"Not given"
                state.value = state.value.copy(
                    userData = userData
                )
            }
            .addOnFailureListener { error->
                state.value = state.value.copy(
                    signInError = error.message ?: "Invalid credentials, please try again!"
                )
            }
    }

    fun resetState(){
        state.value = SignInState()
    }


}