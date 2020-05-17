package com.webster.commerces.ui.commerces.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.webster.commerces.R
import kotlinx.android.synthetic.main.edit_commerce_dialog.*

class EditCommerceDialog(context: Context, private val callback: (EditState) -> Unit) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_commerce_dialog)
        buttonPromo.setOnClickListener { callback.invoke(EditState.DEAL) }
        buttonEdit.setOnClickListener { callback.invoke(EditState.EDIT) }
        buttonDelete.setOnClickListener { callback.invoke(EditState.DELETE) }
        buttonCancel.setOnClickListener { dismiss() }
    }
}

enum class EditState {
    EDIT, DELETE, DEAL
}