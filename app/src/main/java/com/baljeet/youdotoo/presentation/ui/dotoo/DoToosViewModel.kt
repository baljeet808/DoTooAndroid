package com.baljeet.youdotoo.presentation.ui.dotoo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.common.SharedPref
import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DoToosViewModel @Inject constructor(

) : ViewModel() {

    var doToosState = mutableStateOf<List<DoTooWithProfiles>>(listOf())
        private set


    private var projectRef = FirebaseFirestore
        .getInstance()
        .collection("projects")


    fun init(project: ProjectWithProfiles) {
        projectRef
            .document(project.project.id)
            .collection("todos")
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null && error == null) {
                    val doToos = ArrayList<DoTooWithProfiles>()
                    for (doToo in snapshot) {
                        doToos.add(
                            DoTooWithProfiles(
                                project = project.project,
                                profiles = project.profiles,
                                doToo = DoTooItem(
                                    id = doToo.getString("id") ?: "",
                                    title = doToo.getString("title") ?: "",
                                    description = doToo.getString("description") ?: "",
                                    createDate = doToo.getLong("createDate") ?: 0L,
                                    dueDate = doToo.getLong("dueDate") ?: 0L,
                                    priority = doToo.getString("priority") ?: "High",
                                    updatedBy = doToo.getString("updatedBy") ?: "",
                                    done = doToo.getBoolean("done") ?: false,
                                    projectColor = doToo.getLong("projectColor") ?: 4294261839,
                                )
                            )
                        )
                    }
                    doToosState.value = doToos
                } else {
                    //TODO: handle error case error?.message
                }
            }
    }


    fun toggleIsDone(doToo : DoTooWithProfiles){
        val newDoToo = doToo.doToo.copy()
        newDoToo.done = doToo.doToo.done.not()
        newDoToo.updatedBy = SharedPref.userName.plus(" marked this task ").plus(if(newDoToo.done)"completed." else "not completed.")
        projectRef
            .document(doToo.project.id)
            .collection("todos")
            .document(doToo.doToo.id)
            .set(newDoToo)
    }
}