package com.webster.commerces.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val gson = Gson()
fun <T> T.serializeToMap(): Map<String, Any> {
    return convert()
}

inline fun <I, reified O> I.convert(): O {
    val json = gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}