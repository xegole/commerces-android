package com.webster.commerces.utils

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.widget.AppCompatSpinner
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

@BindingAdapter("maxInputText")
fun setMaxInputText(editText: EditText, textLive: MutableLiveData<String>?) {
    editText.afterTextChanged {
        if (editText.lineCount > 2) {
            val size = editText.text.length
            editText.text.delete(size - 1, size)
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

@BindingAdapter("onItemSelected")
fun setOnItemSelected(spinner: AppCompatSpinner, itemSelected: MutableLiveData<Int>?) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, p0: View?, position: Int, id: Long) {
            Log.d("OnItemSelected", "Selected position: $position")
            itemSelected?.value = position
        }
    }
}