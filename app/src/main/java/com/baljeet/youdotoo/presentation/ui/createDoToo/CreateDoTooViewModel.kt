package com.baljeet.youdotoo.presentation.ui.createDoToo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.*
import javax.inject.Inject

/**
 * Updated by Baljeet singh on 15th June, 2023 at 7:00 PM.
 * **/

@HiltViewModel
class CreateDoTooViewModel  @Inject constructor() : ViewModel() {

    private var projectRef = FirebaseFirestore
        .getInstance()
        .collection("projects")

    var createState = mutableStateOf<Boolean>(false)
        private set

    fun createDoToo(
        projectId : String,
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
            .document(projectId)
            .collection("todos")
            .document(newDoTooID)
            .set(newDoToo)
            .addOnSuccessListener {
               createState.value = true
            }
            .addOnFailureListener {error->
                createState.value = false
            }
    }
}