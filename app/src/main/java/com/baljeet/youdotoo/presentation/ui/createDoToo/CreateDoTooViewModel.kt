package com.baljeet.youdotoo.presentation.ui.createDoToo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.models.DoTooItem
import com.baljeet.youdotoo.models.Project
import com.baljeet.youdotoo.util.ApiCallState
import com.baljeet.youdotoo.util.DueDates
import com.baljeet.youdotoo.util.Priorities
import com.baljeet.youdotoo.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.*
import java.util.UUID

/**
 * Updated by Baljeet singh on 15th June, 2023 at 7:00 PM.
 * **/

class CreateDoTooViewModel : ViewModel() {

    private var projectRef = FirebaseFirestore
        .getInstance()
        .collection("projects")

    private val _createState = MutableStateFlow(ApiCallState())
    var createState = _createState.asStateFlow()

    fun createDoToo(
        project : Project,
        name : String,
        description : String,
        priority: Priorities,
        dueDate: DueDates,
        customDate : LocalDateTime?
    ){
        val newDoTooID = UUID.randomUUID().toString()
        val newDoToo = DoTooItem(
            id = newDoTooID,
            title = name,
            description = description,
            priority = priority.toString,
            dueDate = when (dueDate) {
                DueDates.CUSTOM -> {
                    customDate?.toInstant(TimeZone.currentSystemDefault())?.epochSeconds?:0L
                }
                DueDates.INDEFINITE -> {
                    0L
                }
                else -> {
                    dueDate.getExactDateTimeInSecondsFrom1970()
                }
            },
            createDate = java.time.LocalDateTime.now().toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            updatedBy = SharedPref.userName.plus(" created this Dotoo."),
            done = false
        )

        projectRef
            .document(project.id)
            .collection("todos")
            .document(newDoTooID)
            .set(newDoToo)
            .addOnSuccessListener {
                _createState.update {
                    it.copy(
                        isSuccessful =  true
                    )
                }
            }
            .addOnFailureListener {error->
                _createState.update {
                    it.copy(
                        isSuccessful = false,
                        error = error.message
                    )
                }
            }
    }
}