package com.baljeet.youdotoo.presentation.ui.create_task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baljeet.youdotoo.common.DueDates
import com.baljeet.youdotoo.common.Priorities
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.common.getExactDateTimeInSecondsFrom1970
import com.baljeet.youdotoo.data.mappers.toProject
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.Project
import com.baljeet.youdotoo.domain.use_cases.doTooItems.UpsertDoToosUseCase
import com.baljeet.youdotoo.domain.use_cases.project.GetProjectsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toKotlinLocalDateTime
import java.util.*
import javax.inject.Inject


data class CreateTaskState(
    var projects : List<Project> = listOf(),
    var isCreated : Boolean = false
)

@HiltViewModel
class CreateTaskViewModel  @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val upsertDoToosUseCase: UpsertDoToosUseCase,
    private val getProjectsUseCase: GetProjectsUseCase
) : ViewModel() {

    private val projectId: String = checkNotNull(savedStateHandle["projectId"])
    private val projectOwner: Boolean = checkNotNull(savedStateHandle["projectOwner"])

    private var projectRef = FirebaseFirestore
        .getInstance()
        .collection("projects")

    var createState = mutableStateOf(CreateTaskState())
        private set


    init {
        viewModelScope.launch {
            getProjectsUseCase().collect { projects ->
                createState.value = createState.value.copy(
                    projects = projects.map { it.toProject() }
                )
            }
        }
    }


    fun createTask(
        name : String,
        description : String,
        priority: Priorities,
        dueDate: DueDates,
        customDate : LocalDate?,
        selectedProject : Project
    ){


        val newDoTooID = UUID.randomUUID().toString()
        val newDoToo = DoTooItem(
            id = newDoTooID,
            title = name,
            description = description,
            priority = priority.toString,
            dueDate = when (dueDate) {
                DueDates.CUSTOM -> {
                    customDate?.getExactDateTimeInSecondsFrom1970()?:0L
                }
                else -> {
                    dueDate.getExactDate().getExactDateTimeInSecondsFrom1970()
                }
            },
            createDate = java.time.LocalDateTime.now().toKotlinLocalDateTime().toInstant(TimeZone.currentSystemDefault()).epochSeconds,
            updatedBy = SharedPref.userName.plus(" created this Dotoo."),
            done = false
        )

        if (SharedPref.isUserAPro || projectOwner.not()){
            projectRef
                .document(selectedProject.id)
                .collection("todos")
                .document(newDoTooID)
                .set(newDoToo)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        upsertDoToosUseCase(
                            projectId = projectId,
                            dotoos = listOf(newDoToo)
                        )
                    }
                    createState.value = createState.value.copy(
                        isCreated = true
                    )
                }
                .addOnFailureListener {
                    createState.value = createState.value.copy(
                        isCreated = false
                    )
                }
        }else{
            viewModelScope.launch {
                upsertDoToosUseCase(
                    projectId = projectId,
                    dotoos = listOf(newDoToo)
                )
            }
            createState.value = createState.value.copy(
                isCreated = true
            )
        }
    }

    fun resetState(){
        createState.value = CreateTaskState()
    }

}