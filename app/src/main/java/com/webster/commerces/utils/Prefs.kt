package com.webster.commerces.utils

import android.content.Context
import android.content.SharedPreferences
import com.webster.commerces.entity.TypeUser

private const val PREFS_FILENAME = "com.webster.prefs"
private const val CITY_ID = "cityId"
private const val TYPE_USER = "type_user"

class Prefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var cityId: String
        get() = prefs.getString(CITY_ID, Constants.EMPTY_STRING)!!
        set(value) = prefs.edit().putString(CITY_ID, value).apply()

    var remember: String
        get() = prefs.getString(CITY_ID, "") ?: ""
        set(value) = prefs.edit().putString(CITY_ID, value).apply()

    var typeUser: TypeUser
        get() = TypeUser.valueOf(prefs.getString(TYPE_USER, "USER") ?: "USER")
        set(value) = prefs.edit().putString(TYPE_USER, value.name).apply()

    companion object {
        fun clear(context: Context) {
            val preferences = Prefs(context)
            val editor: SharedPreferences.Editor = preferences.prefs.edit()
            editor.clear()
            editor.apply()
        }
    }

}