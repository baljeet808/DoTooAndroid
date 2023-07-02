package com.baljeet.youdotoo.presentation.ui.login

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.data.dto.SignInResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject



@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    var state = mutableStateOf(SignInState())
        private set

    init {
        resetState()
    }


    fun onSignInResult(result: SignInResult) {
        state.value = state.value.copy(
            userId = result.data?.userId,
            signInError = result.errorMessage
        )
    }


    fun performLogin(email: String,password: String){
        if(validateCredential(email, password)){
            viewModelScope.launch{
                login(email, password)
            }
        }
    }


    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                auth.currentUser?.let { user ->
                    SharedPref.isUserLoggedIn = true
                    SharedPref.userId = user.uid
                    getUserInfo(user.uid)
                }
            } else {
                state.value = state.value.copy(
                    userId = null,
                    signInError = task.exception?.message
                        ?: "Invalid credentials, please try again!"
                )
            }
        }
    }


    private fun getUserInfo (userId : String){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .get().addOnSuccessListener { document ->
                    SharedPref.userJoined = document.getLong("joined")?:0L
                    SharedPref.userName = document.getString("avatarUrl")?:""
                    SharedPref.userEmail = document.getString("email")?:""
                    SharedPref.userName = document.getString("name")?:""

                state.value = state.value.copy(
                    userId = userId,
                    signInError = null
                )
            }
            .addOnFailureListener { error->
                state.value = state.value.copy(
                    userId = null,
                    signInError = error.message ?: "Invalid credentials, please try again!"
                )
            }
    }

    private fun validateCredential(email: String, password: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            state.value = state.value.copy(
                userId = null,
                signInError = "Invalidate email. Please provide correct email."
            )
            return false
        }
        if( Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$", password)){
            state.value = state.value.copy(
                userId = null,
                signInError = "Invalid password. Password should be minimum eight characters, at least one uppercase letter, one lowercase letter and one number."
            )
            return false
        }
        return true
    }

    fun resetState() {
        state.value = state.value.copy(
            userId = null,
            signInError = null
        )
    }

}