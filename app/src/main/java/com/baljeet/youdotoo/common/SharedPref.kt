package com.baljeet.youdotoo.common

import android.content.Context
import android.content.SharedPreferences

object SharedPref {

    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPref: SharedPreferences



    fun init(context : Context) {
        sharedPref = context.getSharedPreferences("DoToo_88", MODE )
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isAlertEnabled: Boolean
        get() = sharedPref.getBoolean("isAlertEnabled",true)
        set(value) = sharedPref.edit {
            it.putBoolean("isAlertEnabled",value)
        }

    var isSyncOn: Boolean
        get() = sharedPref.getBoolean("isSyncOn",false)
        set(value) = sharedPref.edit {
            it.putBoolean("isSyncOn",value)
        }

    var userId: String?
        get() = sharedPref.getString("userId",null)
        set(userId) = sharedPref.edit {
            it.putString("userId",userId)
        }

    var userName: String
        get() = sharedPref.getString("userName","")!!
        set(userName) = sharedPref.edit {
            it.putString("userName",userName)
        }



    var userAvatar: String
        get() = sharedPref.getString("userAvatar","")!!
        set(userAvatar) = sharedPref.edit {
            it.putString("userAvatar",userAvatar)
        }

    var userEmail: String
        get() = sharedPref.getString("userEmail","")!!
        set(userEmail) = sharedPref.edit {
            it.putString("userEmail",userEmail)
        }

    var userJoined: Long
        get() = sharedPref.getLong("userJoined",0L)
        set(userJoined) = sharedPref.edit {
            it.putLong("userJoined",userJoined)
        }


    var isUserAPro : Boolean
        get() = sharedPref.getBoolean("isUserAPro", true)
        set(value) = sharedPref.edit { it.putBoolean("isUserAPro", value) }

    var subscriptionIsMonthly : Boolean
        get() = sharedPref.getBoolean("subscribedMonthly", false)
        set(value) = sharedPref.edit{ it.putBoolean("subscribedMonthly", value)}

    var subscriptionIsYearly : Boolean
        get() = sharedPref.getBoolean("subscribedYearly", false)
        set(value) = sharedPref.edit{ it.putBoolean("subscribedYearly", value)}

    fun clearAll(){
        sharedPref.edit().clear().apply()
    }

}