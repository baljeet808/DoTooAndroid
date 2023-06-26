package com.baljeet.youdotoo.presentation.ui.signup

import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.asHashMap
import com.baljeet.youdotoo.domain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor() : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth


    var state = mutableStateOf(SignUpState())
        private set

    init {
        resetState()
    }

    data class SignUpState(
        val userId: String? = null,
        val signUpError: String? = null
    )

    fun performSignup(name : String,email: String,password: String){
        if(validateCredential(name, email, password)){
            viewModelScope.launch{
                signup(name,email, password)
            }
        }
    }


    private fun signup(name : String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                auth.currentUser?.let { user ->
                   updateUserName(userId = user.uid, name = name, email = email)
                }
            } else {
                state.value = state.value.copy(
                    userId = null,
                    signUpError = task.exception?.message
                        ?: "Invalid credentials, please try again!"
                )
            }
        }
    }

    private fun updateUserName (userId : String, name : String, email: String){
        val currentTime = System.currentTimeMillis()
        val commonAvatar = "https://firebasestorage.googleapis.com/v0/b/dotoo-171b4.appspot.com/o/avatar%2Fdo2.png?alt=media&token=701d3864-68e3-445c-9c75-66bc06d44d09"
        val newUser = User(
            id = userId,
            name = name,
            email = email,
            joined = currentTime,
            avatarUrl = commonAvatar
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .set(newUser.asHashMap()).addOnSuccessListener {
                SharedPref.isUserLoggedIn = true
                SharedPref.userId = userId
                SharedPref.userName = name
                SharedPref.userJoined = currentTime
                SharedPref.userAvatar = commonAvatar
                SharedPref.userEmail = email
                state.value = state.value.copy(
                    userId = userId,
                    signUpError = null
                )
            }
            .addOnFailureListener { error->
                state.value = state.value.copy(
                    userId = null,
                    signUpError = error.message ?: "Invalid credentials, please try again!"
                )
            }
    }

    private fun validateCredential(name : String, email: String, password: String): Boolean {
        if (name.isBlank()){
            state.value = state.value.copy(
                userId = null,
                signUpError = "Please provide name."
            )
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            state.value = state.value.copy(
                userId = null,
                signUpError = "Invalidate email. Please provide correct email."
            )
            return false
        }
        if( Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$", password)){
            state.value = state.value.copy(
                userId = null,
                signUpError = "Invalid password. Password should be minimum eight characters, at least one uppercase letter, one lowercase letter and one number."
            )
            return false
        }
        return true
    }

    fun resetState() {
        state.value = state.value.copy(
            userId = null,
            signUpError = null
        )
    }

}