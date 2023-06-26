package com.baljeet.youdotoo


import androidx.lifecycle.ViewModel
import com.baljeet.youdotoo.domain.models.DoTooWithProfiles
import com.baljeet.youdotoo.domain.models.Message
import com.baljeet.youdotoo.domain.models.ProjectWithProfiles
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    val trackerObject = TrackerObject()

}

data class TrackerObject (
    val projects : ArrayList<ProjectWithProfiles> = arrayListOf(),
    var selectedProjectIndex: Int = -1,
    val doToos: ArrayList<DoTooWithProfiles>  = arrayListOf(),
    var selectedDoTooIndex : Int = -1,
    val messages : ArrayList<Message> = arrayListOf(),
    var selectedMessageIndex : Int = -1
)