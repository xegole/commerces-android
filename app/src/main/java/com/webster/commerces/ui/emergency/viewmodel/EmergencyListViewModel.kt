package com.webster.commerces.ui.emergency.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.webster.commerces.AppCore
import com.webster.commerces.entity.Emergency
import com.webster.commerces.utils.FirebaseReferences

private const val CITY_PARAMS = "cityId"

class EmergencyListViewModel(application: Application) : AndroidViewModel(application) {
    private val database = FirebaseDatabase.getInstance()
    private val emergencyReference = database.getReference(FirebaseReferences.EMERGENCY)

    val emergenciesLiveData = MutableLiveData<List<Emergency>>()

    fun loadEmergencies() {
        emergencyReference.orderByChild(CITY_PARAMS).equalTo(AppCore.prefs.cityId)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(firebaseError: DatabaseError) {
                    firebaseError.toException().printStackTrace()
                    emergenciesLiveData.value = emptyList()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val list: ArrayList<Emergency> = ArrayList()
                    for (child in dataSnapshot.children) {
                        val data = child.getValue(Emergency::class.java)!!
                        data.id = child.key ?: ""
                        list.add(data)
                    }
                    emergenciesLiveData.value = list
                }
            })
    }

    fun deleteEmergency(id: String) {
        emergencyReference.child(id).removeValue().addOnSuccessListener {
        }
    }
}