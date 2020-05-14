package com.webster.commerces.utils

import android.util.Log
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText
import com.webster.commerces.extensions.afterTextChanged

@BindingAdapter("liveText")
fun setLiveText(editText: EditText, textLive: MutableLiveData<String>?) {
    editText.afterTextChanged { textChanged ->
        textLive?.apply {
            value = textChanged
        }
    }
}

@BindingAdapter("onTextChanged")
fun setOnTextChanged(editText: EditText, onTextChanged: OnTextChanged) {
    editText.afterTextChanged {
        onTextChanged.onTextChanged(it)
    }
}

interface OnTextChanged {
    fun onTextChanged(text: String)
}

@BindingAdapter("inputText")
fun setInputText(inputEditText: TextInputEditText, inputText: MutableLiveData<String>?) {
    inputEditText.afterTextChanged { textChanged ->
        inputText?.let {
            Log.d("stateChanged", textChanged)
            it.value = textChanged
        }
    }
}