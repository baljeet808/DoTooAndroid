package com.baljeet.youdotoo


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(app : Application) : AndroidViewModel(app) {


    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    data class AuthState(
        val userId: String? = null
    )

    init {
       /* Firebase.auth.addAuthStateListener { auth ->
            auth.currentUser?.uid?.let { userId ->
                SharedPref.userId = userId
                SharedPref.isUserLoggedIn = true
                _state.update {
                    it.copy(
                        userId = userId
                    )
                }
            } ?: kotlin.run {
                SharedPref.userId = null
                SharedPref.isUserLoggedIn = false
                _state.update {
                    it.copy(
                        userId = null
                    )
                }
            }
        }*/
    }
}