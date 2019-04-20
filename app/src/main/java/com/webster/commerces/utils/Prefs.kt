package com.webster.commerces.utils

import android.content.Context
import android.content.SharedPreferences
import com.webster.commerces.utils.Prefs.Values.CITY_ID
import com.webster.commerces.utils.Prefs.Values.PREFS_FILENAME

class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var cityId: String
        get() = prefs.getString(CITY_ID, Constants.EMPTY_STRING)!!
        set(value) = prefs.edit().putString(CITY_ID, value).apply()

    object Values {
        const val PREFS_FILENAME = "com.webster.prefs"
        const val CITY_ID = "city_id"
    }
}