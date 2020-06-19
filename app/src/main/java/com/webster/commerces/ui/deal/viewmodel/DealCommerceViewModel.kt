package com.webster.commerces.ui.deal.viewmodel

import android.app.Application
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.webster.commerces.entity.Commerce
import com.webster.commerces.entity.Deal
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

private const val UID_COMMERCE = "commerce"

class DealCommerceViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val dealsReference = database.getReference(FirebaseReferences.DEALS)
    private val firebaseStorage = FirebaseStorage.getInstance()

    val liveDataDealName = MutableLiveData<String>()
    val liveDataDealPrice = MutableLiveData<String>()
    val liveDataDealDescription = MutableLiveData<String>()
    val liveDataSuccess = MutableLiveData<Boolean>()
    val liveDataLoading = MutableLiveData(false)
    val liveDataDeals = MutableLiveData<List<Deal>>()

    var imageFile: Uri? = null
    private var dealId: String? = null
    private var editMode = false

    fun getDealsByCommerce(commerceId: String) {
        liveDataLoading.value = true
        val query = dealsReference.orderByChild(UID_COMMERCE).equalTo(commerceId)
        query.addListDataListener<Deal> { list, success ->
            liveDataLoading.value = false
            liveDataDeals.value = if (success) list else emptyList()
        }
    }

    fun setEdit(deal: Deal) {
        imageFile = null
        editMode = true
        liveDataDealName.value = deal.name
        liveDataDealDescription.value = deal.description
        liveDataDealPrice.value = deal.price
        dealId = deal.id
    }

    fun saveDeal(commerce: Commerce) = View.OnClickListener {
        val dealName = liveDataDealName.value ?: Constants.EMPTY_STRING
        if (dealName.isNotEmpty()) {
            if (editMode) {
                dealId?.let { id ->
                    updateDeal(id)
                }
            } else if (imageFile != null) {
                val id = dealsReference.push().key ?: Constants.EMPTY_STRING
                saveDealToFirebase(id, dealName, commerce)
            }
        }
    }

    private fun saveDealToFirebase(id: String, dealName: String, commerce: Commerce) {
        val map = HashMap<String, Any>()
        map["id"] = id
        map["name"] = dealName
        map["price"] = liveDataDealPrice.value ?: ""
        map["description"] = liveDataDealDescription.value ?: ""
        map["commerce"] = commerce.commerceId
        map["cityId"] = commerce.cityId

        dealsReference.child(id).setValue(map).addOnSuccessListener {
            imageFile?.let { imageUri ->
                uploadDealImage(imageUri, id)
            }
            liveDataSuccess.value = true
            liveDataDealName.value = ""
            liveDataDealDescription.value = ""
            liveDataDealPrice.value = ""
        }.addOnFailureListener {
            liveDataSuccess.value = false
        }
    }

    private fun updateDeal(dealId: String) {
        editMode = false
        val map = HashMap<String, Any>()
        map["name"] = liveDataDealName.value ?: ""
        map["price"] = liveDataDealPrice.value ?: ""
        map["description"] = liveDataDealDescription.value ?: ""

        dealsReference.child(dealId).updateChildren(map).addOnSuccessListener {
            imageFile?.let { imageUri ->
                uploadDealImage(imageUri, dealId)
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
        dealsReference.child(id).child("image").setValue(urlImage)
    }

    fun deleteDealById(id: String) {
        dealId = null
        dealsReference.child(id).removeValue().addOnSuccessListener {
        }

        val dealImage = firebaseStorage.reference.child("deals/$id.jpg")
        dealImage.delete().addOnSuccessListener {
        }.addOnFailureListener {
        }
    }
}