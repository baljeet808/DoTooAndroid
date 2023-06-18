package com.baljeet.youdotoo.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.util.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LoginViewModel : ViewModel() {

    private var auth: FirebaseAuth = Firebase.auth

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        resetState()
    }

    data class SignInState(
        val userId: String? = null,
        val signInError: String? = null
    )

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
                _state.update {
                    it.copy(
                        userId = null,
                        signInError = task.exception?.message
                            ?: "Invalid credentials, please try again!"
                    )
                }
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

                _state.update {
                    it.copy(
                        userId = userId,
                        signInError = null
                    )
                }
            }
            .addOnFailureListener { error->
                _state.update {
                    it.copy(
                        userId = null,
                        signInError = error.message ?: "Invalid credentials, please try again!"
                    )
                }
            }
    }

    private fun validateCredential(email: String, password: String): Boolean {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
            _state.update {
                it.copy(
                    userId = null,
                    signInError = "Invalidate email. Please provide correct email."
                )
            }
            return false
        }
        if( Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}\$", password)){
            _state.update {
                it.copy(
                    userId = null,
                    signInError = "Invalid password. Password should be minimum eight characters, at least one uppercase letter, one lowercase letter and one number."
                )
            }
            return false
        }
        return true
    }

    fun resetState() {
        _state.update { SignInState() }
    }

}