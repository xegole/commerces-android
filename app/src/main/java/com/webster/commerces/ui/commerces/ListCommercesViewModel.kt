package com.webster.commerces.ui.commerces

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.FirebaseReferences

class ListCommercesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)

    val commercesData = MutableLiveData<List<Commerce>>()

    fun loadListCommerces() {
        commercesReference.addListDataListener<Commerce> { list, success ->
            commercesData.value = if (success) list else emptyList()
        }
    }
}