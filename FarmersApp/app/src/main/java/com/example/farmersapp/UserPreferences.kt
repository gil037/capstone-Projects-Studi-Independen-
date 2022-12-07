package com.example.farmersapp

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val TAG_STATUS = "status"
    private val TAG_TYPE = "usertype"
    private val TAG_APP = "app"

    private val pref: SharedPreferences =
        context.getSharedPreferences(TAG_APP, Context.MODE_PRIVATE)

    var prefStatus: Boolean
        get() = pref.getBoolean(TAG_STATUS, false)
        set(value) = pref.edit().putBoolean(TAG_STATUS, value).apply()

    var prefType: String?
        get() = pref.getString(TAG_TYPE,"")
        set(value) = pref.edit().putBoolean(TAG_TYPE,value)!!.apply()

    fun prefClear(){
        pref.edit().remove(TAG_STATUS).apply()
        pref.edit().remove(TAG_TYPE).apply()
    }
}

private fun SharedPreferences.Editor.putBoolean(
    tagType: String,
    value: String?,
): SharedPreferences.Editor? {
    TODO("Not yet implemented")
}
