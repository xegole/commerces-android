package com.webster.commerces.utils

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.webster.commerces.extensions.afterTextChanged

@BindingAdapter("liveText")
fun setLiveText(editText: EditText, textLive: MutableLiveData<String>?) {
    editText.afterTextChanged { textChanged ->
        textLive?.apply {
            value = textChanged
        }
    }
}