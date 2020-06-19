package com.webster.commerces.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.webster.commerces.entity.User

private const val PREFS_FILENAME = "com.webster.prefs"
private const val CITY_ID = "city_id"
private const val TOKEN_ID = "token_id"
private const val REMEMBER_CITY = "remember_city"
private const val TYPE_USER = "type_user"
private const val USER_DATA = "user_data"

class Prefs(context: Context) {

    private val gson = Gson()

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var cityId: String
        get() = prefs.getString(CITY_ID, "") ?: ""
        set(value) = prefs.edit().putString(CITY_ID, value).apply()

    var tokenDevice: String
        get() = prefs.getString(TOKEN_ID, "") ?: ""
        set(value) = prefs.edit().putString(TOKEN_ID, value).apply()

    var remember: Boolean
        get() = prefs.getBoolean(REMEMBER_CITY, false)
        set(value) = prefs.edit().putBoolean(REMEMBER_CITY, value).apply()

    var user: User?
        get() = gson.fromJson(prefs.getString(USER_DATA, null), User::class.java)
        set(value) {
            val data = gson.toJson(value)
            prefs.edit().putString(USER_DATA, data).apply()
        }

    fun clear() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        fun clear(context: Context) {
            val preferences = Prefs(context)
            val editor: SharedPreferences.Editor = preferences.prefs.edit()
            editor.clear()
            editor.apply()
        }
    }

}