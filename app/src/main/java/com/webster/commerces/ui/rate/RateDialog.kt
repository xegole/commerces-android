package com.webster.commerces.ui.rate

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.AppCore
import com.webster.commerces.R
import com.webster.commerces.extensions.content
import com.webster.commerces.utils.FirebaseReferences
import kotlinx.android.synthetic.main.rating_dialog.*

class RateDialog(context: Context, private val commerceId: String) : Dialog(context) {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    private val prefs by lazy {
        AppCore.prefs
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rating_dialog)
        buttonRate.setOnClickListener {
            uploadComment()
        }
    }

    private fun uploadComment() {
        val mapUser = HashMap<String, Any>()
        prefs.user?.run {
            val map = HashMap<String, Any>()
            map["rate"] = ratingBar.rating
            if (textComment.content().isNotEmpty()) {
                map["comment"] = textComment.content()
            }
            mapUser[uid ?: ""] = map
            commercesReference.child(commerceId).child("comments").updateChildren(mapUser)
                .addOnSuccessListener {
                }
        }
        dismiss()
    }
}