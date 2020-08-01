package com.webster.commerces.base

import android.view.View
import android.widget.AdapterView

abstract class BaseSpinnerActivity<T> : BaseActivity(), AdapterView.OnItemSelectedListener {

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        onItemSelected(parent?.getItemAtPosition(position) as T)
    }

    abstract fun onItemSelected(item: T)
}