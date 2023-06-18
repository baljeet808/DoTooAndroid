package com.baljeet.youdotoo.ui.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.models.User
import com.baljeet.youdotoo.util.SharedPref
import com.baljeet.youdotoo.util.asHashMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupViewModel:ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

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
                _state.update {
                    it.copy(
                        userId = null,
                        signUpError = task.exception?.message
                            ?: "Invalid credentials, please try again!"
                    )
                }
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
                _state.update {
                    it.copy(
                        userId = userId,
                        signUpError = null
                    )
                }
            }
            .addOnFailureListener { error->
                _state.update {
                    it.copy(
                        userId = null,
                        signUpError = error.message ?: "Invalid credentials, please try again!"
                    )
                }
            }
    }

    private fun validateCredential(name : String, email: String, password: String): Boolean {
        if (name.isBlank()){
            _state.update {
                it.copy(
                    userId = null,
                    signUpError = "Please provide name."
                )
            }
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            _state.update {
                it.copy(
                    userId = null,
                    signUpError = "Invalidate email. Please provide correct email."
                )
            }
            return false
        }
        if( Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$", password)){
            _state.update {
                it.copy(
                    userId = null,
                    signUpError = "Invalid password. Password should be minimum eight characters, at least one uppercase letter, one lowercase letter and one number."
                )
            }
            return false
        }
        return true
    }

    fun resetState() {
        _state.update { SignUpState() }
    }

}