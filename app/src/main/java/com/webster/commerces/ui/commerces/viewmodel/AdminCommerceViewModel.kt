package com.webster.commerces.ui.commerces.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.entity.Commerce
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.FirebaseReferences


private const val UID_COMMERCE = "uid"

class AdminCommerceViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FirebaseDatabase.getInstance()
    private val commercesReference = database.getReference(FirebaseReferences.COMMERCES)
    private val storageRef = FirebaseStorage.getInstance().reference
    val commercesListData = MutableLiveData<List<Commerce>>()

    fun getCommercesByUuid(uid: String?) {
        uid?.let {
            val query = commercesReference.orderByChild(UID_COMMERCE).equalTo(uid)
            query.addListDataListener<Commerce> { list, success ->
                commercesListData.value = if (success) list else emptyList()
            }
        }
    }

    fun deleteCommerce(commerceId: String) {
        commercesReference.child(commerceId).removeValue().addOnSuccessListener {
        }
        val imageCommerce = storageRef.child("commerces/${commerceId}.jpg")
        imageCommerce.delete().addOnSuccessListener {
        }.addOnFailureListener {
        }

        for (index in 0..2) {
            val folderImagesCommerce = storageRef.child("commerces/${commerceId}/$index")
            folderImagesCommerce.delete()
        }
    }
}