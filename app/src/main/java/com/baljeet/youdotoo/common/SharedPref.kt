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
    var themeNightDarkColor: Long
        get() = sharedPref.getLong("themeNightDarkColor", 4278848010)
        set(value) = sharedPref.edit {
            it.putLong("themeNightDarkColor",value)
        }
    var themeNightLightColor: Long
        get() = sharedPref.getLong("themeNightLightColor", 4280822313)
        set(value) = sharedPref.edit {
            it.putLong("themeNightLightColor",value)
        }
    var selectedColorPalette: String
        get() = sharedPref.getString("selectedColorPalette", "Eerie Black")!!
        set(value) = sharedPref.edit {
            it.putString("selectedColorPalette",value)
        }

    var themeDayLightColor: Long
        get() = sharedPref.getLong("themeDayLightColor", 4294046456)
        set(value) = sharedPref.edit {
            it.putLong("themeDayLightColor",value)
        }
    var themeDayDarkColor: Long
        get() = sharedPref.getLong("themeDayDarkColor", 4294967295)
        set(value) = sharedPref.edit {
            it.putLong("themeDayDarkColor",value)
        }
    var lastColorsFetchDate: Long
        get() = sharedPref.getLong("lastColorsFetchDate", 0)
        set(value) = sharedPref.edit {
            it.putLong("lastColorsFetchDate",value)
        }
    var themePaletteId: String
        get() = sharedPref.getString("themePaletteId", "")!!
        set(value) = sharedPref.edit {
            it.putString("themePaletteId",value)
        }

    var firebaseToken : String
        get() = sharedPref.getString("firebaseToken", "")!!
        set(value) = sharedPref.edit {
            it.putString("firebaseToken",value)
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
        get() = sharedPref.getBoolean("isUserAPro", false)
        set(value) = sharedPref.edit { it.putBoolean("isUserAPro", value) }

    var subscriptionIsMonthly : Boolean
        get() = sharedPref.getBoolean("subscribedMonthly", false)
        set(value) = sharedPref.edit{ it.putBoolean("subscribedMonthly", value)}

    var subscriptionIsYearly : Boolean
        get() = sharedPref.getBoolean("subscribedYearly", false)
        set(value) = sharedPref.edit{ it.putBoolean("subscribedYearly", value)}


    var deleteTaskWithoutConfirmation : Boolean
        get() = sharedPref.getBoolean("deleteTaskWithoutConfirmation", false)
        set(value) = sharedPref.edit{ it.putBoolean("deleteTaskWithoutConfirmation", value)}


    var doNotBugMeAboutProFeaturesAgain : Boolean
        get() = sharedPref.getBoolean("doNotBugMeAboutProFeaturesAgain", false)
        set(value) = sharedPref.edit{ it.putBoolean("doNotBugMeAboutProFeaturesAgain", value)}


    var showProjectsInitially : Boolean
        get() = sharedPref.getBoolean("showProjectsInitially", true)
        set(value) = sharedPref.edit{ it.putBoolean("showProjectsInitially", value)}


    var showCalendarViewInitially : Boolean
        get() = sharedPref.getBoolean("showCalendarViewInitially", true)
        set(value) = sharedPref.edit{ it.putBoolean("showCalendarViewInitially", value)}


    fun clearAll(){
        sharedPref.edit().clear().apply()
    }

}