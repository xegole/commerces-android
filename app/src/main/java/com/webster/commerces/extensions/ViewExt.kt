package com.webster.commerces.extensions

import android.app.Activity
import android.content.Intent
import android.view.View

fun View.goActivity(clazz: Class<*>, isFinish: Boolean = false) {
    val intent = Intent(context, clazz)
    context.startActivity(intent)
    if (isFinish) {
        (context as Activity).finish()
    }
}