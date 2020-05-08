package com.webster.commerces.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.goActivity(clazz: Class<*>, isFinish: Boolean = false) {
    val intent = Intent(context, clazz)
    context.startActivity(intent)
    if (isFinish) {
        (context as Activity).finish()
    }
}

fun View.hideKeyboard() {
    val inputMethodManager =
        this.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}