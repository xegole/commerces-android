package com.webster.commerces.ui.emergency.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import com.webster.commerces.R
import com.webster.commerces.entity.City
import com.webster.commerces.entity.Emergency
import com.webster.commerces.extensions.addListDataListener
import com.webster.commerces.extensions.getString
import com.webster.commerces.extensions.serializeToMap
import com.webster.commerces.utils.Constants
import com.webster.commerces.utils.FirebaseReferences

class CreateEmergencyViewModel(application: Application) : AndroidViewModel(application) {

    private val database = FirebaseDatabase.getInstance()
    private val referenceCities = database.getReference(FirebaseReferences.CITIES)
    private val emergencyReference = database.getReference(FirebaseReferences.EMERGENCY)

    val emergencyNumberLiveData = MutableLiveData<String>()
    val emergencyWhatsappLiveData = MutableLiveData<String>()
    val descriptionLiveData = MutableLiveData<String>()
    val citiesLiveData = MutableLiveData<List<City>>()
    val typeLiveData = MutableLiveData<Int>()
    val loadingLiveData = MutableLiveData(false)
    val successLiveData = MutableLiveData<String>()

    var city: City? = null
    var currentEmergency = ""

    fun setInitialData(emergency: Emergency) {
        descriptionLiveData.value = emergency.description
        emergencyNumberLiveData.value = emergency.number
        emergencyWhatsappLiveData.value = emergency.whatsapp
        currentEmergency = emergency.id
    }

    val onCreateEmergency = View.OnClickListener {
        val type = typeLiveData.value ?: -1
        if (city != null && type != -1) {
            val emergencyNumber = emergencyNumberLiveData.value ?: ""
            val whatsapp = emergencyWhatsappLiveData.value ?: ""
            val description = descriptionLiveData.value ?: ""
            if (currentEmergency.isNotEmpty()) {
                val emergency =
                    Emergency(
                        type,
                        emergencyNumber,
                        whatsapp,
                        description,
                        city?.cityId ?: "",
                        currentEmergency
                    )
                updateEmergency(emergency)
            } else {
                val emergency =
                    Emergency(type, emergencyNumber, whatsapp, description, city?.cityId ?: "", "")
                saveEmergencyToFirebase(emergency)
            }
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
        emergency.id = id
        emergencyReference.child(id).setValue(emergency).addOnSuccessListener {
            loadingLiveData.value = false
            successLiveData.value = ""
        }.addOnFailureListener {
            loadingLiveData.value = false
            successLiveData.value = it.localizedMessage
        }
    }

    private fun updateEmergency(emergency: Emergency) {
        loadingLiveData.value = true
        emergencyReference.child(emergency.id).updateChildren(emergency.serializeToMap())
            .addOnSuccessListener {
                loadingLiveData.value = false
                successLiveData.value = getString(R.string.label_update_emergency_success)
            }.addOnFailureListener {
            loadingLiveData.value = false
            successLiveData.value = it.localizedMessage
        }
    }

    fun clearFields() {
        emergencyNumberLiveData.value = ""
        emergencyWhatsappLiveData.value = ""
        descriptionLiveData.value = ""
    }
}