package com.webster.commerces.ui.deal.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.webster.commerces.R
import kotlinx.android.synthetic.main.edit_deal_dialog.*

class EditDealDialog(context: Context, private val callback: (EditDealState) -> Unit) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_deal_dialog)
        buttonEdit.setOnClickListener { callback.invoke(EditDealState.EDIT) }
        buttonDelete.setOnClickListener { callback.invoke(EditDealState.DELETE) }
        buttonCancel.setOnClickListener { dismiss() }
    }
}

enum class EditDealState {
    EDIT, DELETE
}