package com.webster.commerces.extensions

import android.widget.EditText
import com.webster.commerces.utils.Constants

fun EditText.content() = this.text.toString()

fun EditText.clear() = this.setText(Constants.EMPTY_STRING)