package com.baljeet.youdotoo.presentation.ui.dashboard

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun signOut(){
        firebaseAuth.signOut()
    }

}