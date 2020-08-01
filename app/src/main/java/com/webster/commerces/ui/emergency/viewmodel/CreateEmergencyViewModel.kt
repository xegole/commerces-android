package com.webster.commerces.ui.emergency.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Emergency
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

class CreateEmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val referenceCities = database.getReference(FirebaseReferences.CITIES)
    private val emergencyReference = database.getReference(FirebaseReferences.EMERGENCY)

    val emergencyNumberLiveData = MutableLiveData<String>()
    val emergencyWhatsappLiveData = MutableLiveData<String>()
    val dialNumberLiveData = MutableLiveData<String>()
    val citiesLiveData = MutableLiveData<List<City>>()
    val typeLiveData = MutableLiveData<Int>()
    val loadingLiveData = MutableLiveData<Boolean>(false)
    val showDialNumber = MutableLiveData<Boolean>(false)
    val successLiveData = MutableLiveData<String>()

    var city: City? = null

    val onCreateEmergency = View.OnClickListener {
        val type = typeLiveData.value ?: -1
        if (city != null && type != -1) {
            val emergencyNumber = emergencyNumberLiveData.value ?: ""
            val whatsapp = emergencyWhatsappLiveData.value ?: ""
            val dialNumber = dialNumberLiveData.value ?: "0"
            val emergency =
                Emergency(type, emergencyNumber, whatsapp, dialNumber.toInt(), city?.cityId ?: "")
            saveEmergencyToFirebase(emergency)
        }
    }

    fun loadData() {
        referenceCities.addListDataListener<City> { list, success ->
            if (success) {
                citiesLiveData.value = list
            }
        }
    }

    private fun saveEmergencyToFirebase(emergency: Emergency) {
        loadingLiveData.value = true
        val id = emergencyReference.push().key ?: Constants.EMPTY_STRING
        emergencyReference.child(id).setValue(emergency).addOnSuccessListener {
            loadingLiveData.value = false
            successLiveData.value = ""
        }.addOnFailureListener {
            loadingLiveData.value = false
            successLiveData.value = it.localizedMessage
        }
    }
}