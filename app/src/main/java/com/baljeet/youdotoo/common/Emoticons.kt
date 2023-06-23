package com.baljeet.youdotoo.common

import com.baljeet.youdotoo.R

/**
 * Updated by Baljeet singh
 * **/


fun String.getEmoticonResourceId(): Int{
    return when(this){
        "amazed" ->{
            R.drawable.amazed
        }
        "anger" ->{
            R.drawable.anger
        }
        "crying_face" ->{
            R.drawable.crying_face
        }
        "cute_face" ->{
            R.drawable.cute_face
        }
        "devil" ->{
            R.drawable.devil
        }
        "feeling_good" ->{
            R.drawable.feeling_good
        }
        "hi" ->{
            R.drawable.hi
        }
        "loving_it" ->{
            R.drawable.loving_it
        }
        "not_impressed" ->{
            R.drawable.not_impressed
        }
        "not_today" ->{
            R.drawable.not_today
        }
        "omg" ->{
            R.drawable.omg
        }
        "party" ->{
            R.drawable.party
        }
        "puzzeled" ->{
            R.drawable.puzzeled
        }
        "sad_face" ->{
            R.drawable.sad_face
        }
        "scared" ->{
            R.drawable.scared
        }
        "sleeping" ->{
            R.drawable.sleeping
        }
        "thinking" ->{
            R.drawable.thinking
        }
        "wow" ->{
            R.drawable.wow
        }
        "wynk" ->{
            R.drawable.wynk
        }
        else ->{
            R.drawable.yummy
        }
    }
}