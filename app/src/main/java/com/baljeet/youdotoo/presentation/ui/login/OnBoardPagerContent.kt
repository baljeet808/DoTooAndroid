package com.baljeet.youdotoo.presentation.ui.login

import androidx.annotation.DrawableRes
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.ConstFirstScreenDescription
import com.baljeet.youdotoo.common.ConstSecScreenDescription
import com.baljeet.youdotoo.common.ConstThirdScreenDescription

/**
 * Updated by Baljeet singh.
 * **/
data class OnBoardPagerContent(
    val title : String,
    @DrawableRes val res : Int,
    val description : String
)


fun getOnBoardPagerContentList () : List<OnBoardPagerContent>{
    return listOf(
        OnBoardPagerContent(
            title = "Separate Chat For EveryTask!",
            res = R.drawable.chat_illustration,
            description = ConstSecScreenDescription
        ),
        OnBoardPagerContent(
            title = "Organize Tasks Easily With Dotoos!",
            res = R.drawable.set_reminders,
            description = ConstThirdScreenDescription
        ),
        OnBoardPagerContent(
            title = "You Do It Too!",
            res = R.drawable.todo_illustration,
            description = ConstFirstScreenDescription
        )
    )
}
