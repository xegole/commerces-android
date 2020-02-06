package com.webster.commerces.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.webster.commerces.utils.Constants

fun EditText.content() = this.text.toString()

fun EditText.clear() = this.setText(Constants.EMPTY_STRING)

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}