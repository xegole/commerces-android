package com.webster.commerces.ui.deal

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

class DealCommerceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val dealsReference = database.getReference(FirebaseReferences.DEALS)
    private val firebaseStorage = FirebaseStorage.getInstance()

    val liveDataDealName = MutableLiveData<String>()
    val liveDataDealDescription = MutableLiveData<String>()
    val liveDataSuccess = MutableLiveData<Boolean>()

    var imageFile: Uri? = null

    fun saveDeal() = View.OnClickListener {
        val dealName = liveDataDealName.value ?: Constants.EMPTY_STRING
        if (dealName.isNotEmpty() && imageFile != null) {
            val id = dealsReference.push().key ?: Constants.EMPTY_STRING
            saveDealToFirebase(id, dealName)
        }
    }

    private fun saveDealToFirebase(id: String, dealName: String) {
        val map = HashMap<String, Any>()
        map["deal_id"] = id
        map["name"] = dealName
        map["description"] = liveDataDealDescription.value ?: ""

        dealsReference.child(id).setValue(map).addOnSuccessListener {
            imageFile?.let { imageUri ->
                uploadDealImage(imageUri, id)
            }
            liveDataSuccess.value = true
            liveDataDealName.value = ""
            liveDataDealDescription.value = ""
        }.addOnFailureListener {
            liveDataSuccess.value = false
        }
    }

    private fun uploadDealImage(file: Uri, dealId: String) {
        val routeFireStorage = "deals/$dealId.jpg"
        val storageReference = firebaseStorage.reference.child(routeFireStorage)
        val uploadTask = storageReference.putFile(file)
        uploadTask
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    updateDealImage(dealId, it.toString())
                }
            }
            .addOnProgressListener {
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun updateDealImage(id: String, urlImage: String?) {
        dealsReference.child(id).child("deal_image").setValue(urlImage)
    }

}