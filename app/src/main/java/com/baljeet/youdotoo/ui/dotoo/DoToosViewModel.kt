package com.baljeet.youdotoo.ui.dotoo

import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.models.DoTooItem
import com.baljeet.youdotoo.models.DoTooWithProfiles
import com.baljeet.youdotoo.models.ProjectWithProfiles
import com.baljeet.youdotoo.util.SharedPref
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DoToosViewModel : ViewModel() {

    private val _doToosState = MutableStateFlow(DoToosViewStates())
    val doToosState = _doToosState.asStateFlow()

    data class DoToosViewStates(
        val doToos: List<DoTooWithProfiles>? = null,
        var error: String? = null
    )

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
                                    done = doToo.getBoolean("done") ?: false
                                )
                            )
                        )
                    }
                    _doToosState.update {
                        it.copy(
                            doToos = doToos,
                            error = null
                        )
                    }
                } else {
                    _doToosState.update {
                        it.copy(
                            doToos = null,
                            error = error?.message
                        )
                    }
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